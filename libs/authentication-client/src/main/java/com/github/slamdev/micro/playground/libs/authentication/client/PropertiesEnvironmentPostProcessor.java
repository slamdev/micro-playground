package com.github.slamdev.micro.playground.libs.authentication.client;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "defaultProperties";

    private final Map<String, String> properties = toProperties("default.properties");

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private static Map<String, String> toProperties(String path) {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(path)) {
            properties.load(is);
        }
        return (Map) properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        addOrReplace(environment.getPropertySources(), (Map) properties);
    }

    @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
    private void addOrReplace(MutablePropertySources propertySources, Map<String, Object> map) {
        MapPropertySource target = null;
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            PropertySource<?> source = propertySources.get(PROPERTY_SOURCE_NAME);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (!target.containsProperty(entry.getKey())) {
                        target.getSource().put(entry.getKey(), map.get(entry.getKey()));
                    }
                }
            }
        }
        if (target == null) {
            target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
        }
        if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
            propertySources.addLast(target);
        }
    }
}
