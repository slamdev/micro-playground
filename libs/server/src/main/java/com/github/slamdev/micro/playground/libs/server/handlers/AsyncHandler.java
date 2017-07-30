package com.github.slamdev.micro.playground.libs.server.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public interface AsyncHandler extends HttpHandler {

    default void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        process(exchange);
    }

    void process(HttpServerExchange exchange);
}
