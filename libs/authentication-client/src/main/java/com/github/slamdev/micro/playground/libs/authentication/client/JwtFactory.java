package com.github.slamdev.micro.playground.libs.authentication.client;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static com.nimbusds.jose.JWSAlgorithm.HS256;
import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class JwtFactory {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @SneakyThrows
    public SignedJWT create(String subject, List<String> roles) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer("http://micro-playground.com")
                .subject(subject)
                .audience("http://example.com")
                .expirationTime(Date.from(Instant.now().plus(1, DAYS)))
                .issueTime(Date.from(Instant.now()))
                .claim("roles", roles);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(HS256), builder.build());
        signedJWT.sign(new MACSigner(jwtSecret.getBytes(StandardCharsets.UTF_8)));
        return signedJWT;
    }
}
