package com.github.slamdev.micro.playground.libs.authentication.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
public class JwtAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    private static final String HEADER_PREFIX = "Bearer";

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(h -> h.startsWith(HEADER_PREFIX))
                .map(h -> h.replace(HEADER_PREFIX, ""))
                .map(String::trim)
                .orElse(null);
    }
}
