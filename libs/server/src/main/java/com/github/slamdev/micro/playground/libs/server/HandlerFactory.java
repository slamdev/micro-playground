package com.github.slamdev.micro.playground.libs.server;

import com.github.slamdev.micro.playground.libs.server.handlers.LogbackAccessHandler;
import io.undertow.server.HttpHandler;

public final class HandlerFactory {

    private HandlerFactory() {
        // Utility class
    }

    public static LogbackAccessHandler loggingHandler(HttpHandler next) {
        return new LogbackAccessHandler(next);
    }
}
