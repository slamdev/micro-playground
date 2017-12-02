package com.github.slamdev.micro.playground.libs.spring.conventions;

import org.springframework.context.annotation.Import;

@Import({
        AuditingConfiguration.class,
        ClientExceptionHandler.class
})
class ConfigurationsImporter {
}
