package com.github.slamdev.micro.playground.libs.spring.conventions;

class LibraryEnvironmentPostProcessor extends PropertiesEnvironmentPostProcessor {

    public LibraryEnvironmentPostProcessor() {
        super("spring-conventions.properties");
    }
}
