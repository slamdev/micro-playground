package com.github.slamdev.micro.playground.libs.spring.convetions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
class ClientExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public void handleClientRequests(HttpServletResponse response, RestClientResponseException e) throws IOException {
        response.sendError(e.getRawStatusCode(), e.getStatusText());
    }
}
