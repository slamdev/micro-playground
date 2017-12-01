package com.github.slamdev.micro.playground.libs.spring.convetions;

import org.springframework.context.annotation.Import;

@Import({
        AuditingConfiguration.class,
        ClientExceptionHandler.class
})
class ConfigurationsImporter {
}
