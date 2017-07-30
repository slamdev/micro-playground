package com.github.slamdev.micro.playground.services.diespringdie;

import com.github.slamdev.micro.playground.libs.server.Server;
import com.github.slamdev.micro.playground.libs.server.handlers.AsyncHandler;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.slamdev.micro.playground.libs.server.handlers.ConfigHandler.CONFIG;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Starting server");
        new Server().run(handler());
    }

    private static HttpHandler handler() {
        return Handlers.path(((AsyncHandler) exchange -> exchange.getResponseSender().send("Hello World " + getPort(exchange))))
                .addPrefixPath("foo", exchange -> exchange.getResponseSender().send("fooo"))
                .addPrefixPath("bar", exchange -> exchange.getResponseSender().send("bar"));
    }

    private static int getPort(HttpServerExchange exchange) {
        return exchange.getAttachment(CONFIG).getInt("server.port");
    }
}
