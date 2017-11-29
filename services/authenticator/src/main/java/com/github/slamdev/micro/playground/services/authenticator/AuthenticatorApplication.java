package com.github.slamdev.micro.playground.services.authenticator;

import com.github.slamdev.micro.playground.libs.authentication.client.JwtFactory;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
@SpringBootApplication
@RestController
public class AuthenticatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticatorApplication.class, args);
    }

    private final JwtFactory jwtFactory;

    @SneakyThrows
    @Secured("ROLE_ANONYMOUS" /* only anonymous users can request a new token */)
    @PutMapping("api/token")
    public String createToken(@RequestBody Credentials credentials) {
        // TODO: credentials verification check
        SignedJWT jwt = jwtFactory.create(credentials.getUsername(), asList("user", "admin"));
        return jwt.serialize();
    }

    @Value
    public static class Credentials {
        String username;
        String password;
    }
}
