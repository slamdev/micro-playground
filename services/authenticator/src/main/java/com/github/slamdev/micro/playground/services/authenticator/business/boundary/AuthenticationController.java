package com.github.slamdev.micro.playground.services.authenticator.business.boundary;

import com.github.slamdev.micro.playground.java.lang.Optionals;
import com.github.slamdev.micro.playground.libs.authentication.client.JwtFactory;
import com.github.slamdev.micro.playground.services.authenticator.api.AuthenticatorApi;
import com.github.slamdev.micro.playground.services.authenticator.business.control.UserRepository;
import com.github.slamdev.micro.playground.services.authenticator.business.entity.User;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Supplier;

import static com.github.slamdev.micro.playground.java.lang.Exceptions.transformException;
import static com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role.USER;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticatorApi {

    private final JwtFactory jwtFactory;

    private final UserRepository userRepository;

    private <T> T handleDuplicateException(Supplier<T> action) {
        return transformException(action, DuplicateKeyException.class,
                e -> new HttpClientErrorException(CONFLICT, e.getCause().getMessage()));
    }

    @Override
    public com.github.slamdev.micro.playground.services.authenticator.api.User createUser(com.github.slamdev.micro.playground.services.authenticator.api.Credential credential) {
        User user = User.builder()
                .email(credential.getEmail())
                .password(credential.getPassword())
                .role(USER)
                .build();
        User savedUser = handleDuplicateException(() -> userRepository.save(user));
        return com.github.slamdev.micro.playground.services.authenticator.api.User.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .role(Optionals.enumeration(com.github.slamdev.micro.playground.services.authenticator.api.User.Role.class, savedUser.getRole().name()).orElse(null))
                .build();
    }

    @Override
    public String generateToken(com.github.slamdev.micro.playground.services.authenticator.api.Credential credential) {
        return userRepository.findByEmail(credential.getEmail())
                .filter(user -> user.getPassword().equals(credential.getPassword()))
                .map(user -> jwtFactory.create(user.getEmail(), singletonList(user.getRole().toString())))
                .map(SignedJWT::serialize)
                .orElseThrow(() -> new HttpClientErrorException(UNAUTHORIZED));
    }
}
