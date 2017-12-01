package com.github.slamdev.micro.playground.libs.authentication.client;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

@Getter
class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 42L;

    private final OAuth2User principal;

    private final String accessToken;

    JwtAuthenticationToken(OAuth2User principal,
                           Collection<? extends GrantedAuthority> authorities,
                           String accessToken) {
        super(authorities);
        this.principal = principal;
        this.accessToken = accessToken;
    }

    @Override
    public Object getCredentials() {
        return "";
    }
}
