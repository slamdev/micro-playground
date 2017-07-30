package com.github.slamdev.micro.playground.libs.server;

import com.github.slamdev.micro.playground.libs.server.handlers.ConfigHandler;
import com.github.slamdev.micro.playground.libs.server.handlers.LogbackAccessHandler;
import com.typesafe.config.Config;
import io.undertow.server.HttpHandler;

public final class HandlerFactory {

    private HandlerFactory() {
        // Utility class
    }

    public static LogbackAccessHandler loggingHandler(HttpHandler next) {
        return new LogbackAccessHandler(next);
    }

    public static ConfigHandler configHandler(HttpHandler nextHandler, Config config) {
        return new ConfigHandler(nextHandler, config);
    }
}
