package com.github.slamdev.micro.playground.services.authenticator.business.boundary;

import com.github.slamdev.micro.playground.libs.authentication.client.JwtFactory;
import com.github.slamdev.micro.playground.services.authenticator.api.AuthenticatorApi;
import com.github.slamdev.micro.playground.services.authenticator.api.CredentialDto;
import com.github.slamdev.micro.playground.services.authenticator.api.TokenDto;
import com.github.slamdev.micro.playground.services.authenticator.api.UserDto;
import com.github.slamdev.micro.playground.services.authenticator.business.control.UserRepository;
import com.github.slamdev.micro.playground.services.authenticator.business.entity.User;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper = new ModelMapper();

    private <T> T handleDuplicateException(Supplier<T> action) {
        return transformException(action, DuplicateKeyException.class,
                e -> new HttpClientErrorException(CONFLICT, e.getCause().getMessage()));
    }

    @Override
    public UserDto createUser(CredentialDto credential) {
        User user = User.builder()
                .email(credential.getEmail())
                .password(passwordEncoder.encode(credential.getPassword()))
                .role(singletonList(USER))
                .build();
        User savedUser = handleDuplicateException(() -> userRepository.save(user));
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public TokenDto generateToken(CredentialDto credential) {
        String token = userRepository.findByEmail(credential.getEmail())
                .filter(user -> passwordEncoder.matches(credential.getPassword(), user.getPassword()))
                .map(user -> jwtFactory.create(user.getEmail(), singletonList(user.getRole().toString())))
                .map(SignedJWT::serialize)
                .orElseThrow(() -> new HttpClientErrorException(UNAUTHORIZED));
        return TokenDto.builder()
                .token(token)
                .build();
    }
}
