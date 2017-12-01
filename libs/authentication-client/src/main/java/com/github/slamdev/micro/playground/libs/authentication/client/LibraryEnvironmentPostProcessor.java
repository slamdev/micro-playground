package com.github.slamdev.micro.playground.libs.authentication.client;

import com.github.slamdev.micro.playground.libs.spring.convetions.PropertiesEnvironmentPostProcessor;

class LibraryEnvironmentPostProcessor extends PropertiesEnvironmentPostProcessor {

    public LibraryEnvironmentPostProcessor() {
        super("authentication-client.properties");
    }
}
