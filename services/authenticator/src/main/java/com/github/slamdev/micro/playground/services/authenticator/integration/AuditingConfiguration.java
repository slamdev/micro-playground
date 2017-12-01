package com.github.slamdev.micro.playground.services.authenticator.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return this::email;
    }

    private Optional<String> email() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(context.getAuthentication())
                .map(Authentication::getName);
    }
}
