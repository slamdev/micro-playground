package com.github.slamdev.micro.playground.libs.authentication.client;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String HEADER_PREFIX = "Bearer";

    public JwtAuthenticationFilter() {
        super("/**");
        setAuthenticationSuccessHandler((request, response, authentication) -> log.info("Authenticated with {}", authentication));
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        super.doFilter(req, res, chain);
        HttpServletResponse response = (HttpServletResponse) res;
        HttpStatus status = HttpStatus.resolve(response.getStatus());
        if (status.is2xxSuccessful()) {
            chain.doFilter(req, res);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return token(request)
                .map(t -> authenticate(t, request))
                .orElse(null);
    }

    private Authentication authenticate(String token, HttpServletRequest request) {
        JWT parsedJwt;
        try {
            parsedJwt = JWTParser.parse(token);
        } catch (ParseException e) {
            throw new BadCredentialsException("Unable to parse JWT token", e);
        }
        JwtPreAuthenticatedToken authentication = new JwtPreAuthenticatedToken(parsedJwt);
        authentication.setDetails(authenticationDetailsSource.buildDetails(request));
        authentication.setAuthenticated(false);
        return getAuthenticationManager().authenticate(authentication);
    }

    private Optional<String> token(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(h -> h.startsWith(HEADER_PREFIX))
                .map(h -> h.replace(HEADER_PREFIX, ""))
                .map(String::trim);
    }
}
