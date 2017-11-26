package com.github.slamdev.micro.playground.libs.authentication.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@SpringBootApplication
@RestController
public class OneApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneApplication.class, args);
    }

    @GetMapping("api/profile1")
    public Principal profile1(HttpServletRequest request) {
        return request.getUserPrincipal();
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("api/profile2")
    public String profile2() {
        return "server 1 profile 2";
    }
}
