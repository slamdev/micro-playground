package com.github.slamdev.micro.playground.libs.authentication.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Qualifier("rest-api")
@Component
class SecuredApiRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(a -> a instanceof JwtAuthenticationToken)
                .map(JwtAuthenticationToken.class::cast)
                .map(JwtAuthenticationToken::getAccessToken)
                .map(" "::concat)
                .map(JwtAuthenticationFilter.HEADER_PREFIX::concat)
                .ifPresent(tokent -> request.getHeaders().add(HttpHeaders.AUTHORIZATION, tokent));
        return execution.execute(request, body);
    }
}
