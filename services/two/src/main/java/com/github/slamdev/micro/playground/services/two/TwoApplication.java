package com.github.slamdev.micro.playground.services.two;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoApplication.class, args);
    }

    @GetMapping("api/profile1")
    public String profile1() {
        return "";
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("api/profile2")
    public String profile2() {
        return "";
    }
}
