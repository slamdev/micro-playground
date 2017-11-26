package com.github.slamdev.micro.playground.libs.authentication.client;

import com.nimbusds.jwt.JWT;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@Getter
public class JwtPreAuthenticatedToken extends AbstractAuthenticationToken {

    private final JWT principal;

    public JwtPreAuthenticatedToken(JWT principal) {
        super(Collections.emptySet());
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }
}
