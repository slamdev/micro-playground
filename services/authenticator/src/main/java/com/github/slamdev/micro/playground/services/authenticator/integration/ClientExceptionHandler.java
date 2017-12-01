package com.github.slamdev.micro.playground.services.authenticator.integration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public void handleClientRequests(HttpServletResponse response, RestClientResponseException e) throws IOException {
        response.sendError(e.getRawStatusCode(), e.getStatusText());
    }
}
