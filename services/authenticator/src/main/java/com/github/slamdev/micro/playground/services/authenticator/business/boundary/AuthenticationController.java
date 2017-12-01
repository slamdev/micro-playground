package com.github.slamdev.micro.playground.services.authenticator.business.boundary;

import com.github.slamdev.micro.playground.libs.authentication.client.JwtFactory;
import com.github.slamdev.micro.playground.services.authenticator.business.control.UserRepository;
import com.github.slamdev.micro.playground.services.authenticator.business.entity.Credential;
import com.github.slamdev.micro.playground.services.authenticator.business.entity.User;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.ADMIN_ROLE;
import static com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.ANONYMOUS_ROLE;
import static com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role.USER;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtFactory jwtFactory;

    private final UserRepository userRepository;

    @SneakyThrows
    @Secured(ANONYMOUS_ROLE /* only anonymous users can request a new token */)
    @PutMapping("token")
    public String createToken(@RequestBody Credential credentials) {
        return userRepository.findByEmail(credentials.getEmail())
                .filter(user -> user.getPassword().equals(credentials.getPassword()))
                .map(user -> jwtFactory.create(user.getEmail(), singletonList(user.getRole().toString())))
                .map(SignedJWT::serialize)
                .orElseThrow(() -> new HttpClientErrorException(UNAUTHORIZED));
    }

    @Secured(ADMIN_ROLE)
    @PutMapping("user")
    public User createUser(@RequestBody Credential credentials) {
        User user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .role(USER)
                .build();
        return handleDuplicateException(() -> userRepository.save(user));
    }

    private <T> T handleDuplicateException(Supplier<T> action) {
        return transformException(action, DuplicateKeyException.class,
                e -> new HttpClientErrorException(CONFLICT, e.getCause().getMessage()));
    }

    private <T, E extends Exception, K extends RuntimeException> T transformException(Supplier<T> action,
                                                                                      Class<E> exceptionType,
                                                                                      Function<E, K> transformer) {
        try {
            return action.get();
        } catch (Exception e) {
            if (exceptionType.isInstance(e)) {
                throw transformer.apply(exceptionType.cast(e));
            } else {
                throw e;
            }
        }
    }
}
