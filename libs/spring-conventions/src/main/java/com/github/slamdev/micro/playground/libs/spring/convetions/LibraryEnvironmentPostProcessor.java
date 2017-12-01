package com.github.slamdev.micro.playground.libs.spring.convetions;

class LibraryEnvironmentPostProcessor extends PropertiesEnvironmentPostProcessor {

    public LibraryEnvironmentPostProcessor() {
        super("spring-conventions.properties");
    }
}
