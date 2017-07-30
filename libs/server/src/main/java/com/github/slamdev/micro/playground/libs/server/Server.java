package com.github.slamdev.micro.playground.libs.server;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.slamdev.micro.playground.libs.server.HandlerFactory.loggingHandler;
import static io.undertow.Handlers.exceptionHandler;
import static io.undertow.server.handlers.ExceptionHandler.THROWABLE;

public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private static void handleException(HttpServerExchange exchange) {
        Throwable throwable = exchange.getAttachment(THROWABLE);
        LOGGER.info("{}", throwable.getMessage());
    }

    public void run(HttpHandler baseHandler) {
        ExceptionHandler exceptionHandler = exceptionHandler(baseHandler)
                .addExceptionHandler(Throwable.class, Server::handleException);
        HttpHandler handler = loggingHandler(exceptionHandler);
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(handler).build();
        server.start();
    }
}
