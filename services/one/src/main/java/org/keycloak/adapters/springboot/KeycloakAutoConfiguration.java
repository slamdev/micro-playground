/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.adapters.springboot;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Keycloak authentication integration for Spring Boot
 *
 * @author <a href="mailto:jimmidyson@gmail.com">Jimmi Dyson</a>
 * @version $Revision: 1 $
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)
@ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
public class KeycloakAutoConfiguration {

    private KeycloakSpringBootProperties keycloakProperties;

    @Autowired
    public void setKeycloakSpringBootProperties(KeycloakSpringBootProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        KeycloakSpringBootConfigResolver.setAdapterConfig(keycloakProperties);
    }


    @Bean
    public WebServerFactoryCustomizer getKeycloakContainerCustomizer() {
        return (WebServerFactoryCustomizer<TomcatServletWebServerFactory>) configurableEmbeddedServletContainer -> {
            configurableEmbeddedServletContainer.addContextValves(new KeycloakAuthenticatorValve());
            configurableEmbeddedServletContainer.addContextCustomizers(tomcatKeycloakContextCustomizer());
        };
    }

    @Bean
    @ConditionalOnClass(name = {"org.apache.catalina.startup.Tomcat"})
    public TomcatContextCustomizer tomcatKeycloakContextCustomizer() {
        return new KeycloakTomcatContextCustomizer(keycloakProperties);
    }

    static class KeycloakTomcatContextCustomizer implements TomcatContextCustomizer {

        private final KeycloakSpringBootProperties keycloakProperties;

        public KeycloakTomcatContextCustomizer(KeycloakSpringBootProperties keycloakProperties) {
            this.keycloakProperties = keycloakProperties;
        }

        @Override
        public void customize(Context context) {
            LoginConfig loginConfig = new LoginConfig();
            loginConfig.setAuthMethod("KEYCLOAK");
            context.setLoginConfig(loginConfig);

            Set<String> authRoles = new HashSet<String>();
            for (KeycloakSpringBootProperties.SecurityConstraint constraint : keycloakProperties.getSecurityConstraints()) {
                for (String authRole : constraint.getAuthRoles()) {
                    if (!authRoles.contains(authRole)) {
                        context.addSecurityRole(authRole);
                        authRoles.add(authRole);
                    }
                }
            }

            for (KeycloakSpringBootProperties.SecurityConstraint constraint : keycloakProperties.getSecurityConstraints()) {
                SecurityConstraint tomcatConstraint = new SecurityConstraint();

                for (String authRole : constraint.getAuthRoles()) {
                    tomcatConstraint.addAuthRole(authRole);
                }

                for (KeycloakSpringBootProperties.SecurityCollection collection : constraint.getSecurityCollections()) {
                    SecurityCollection tomcatSecCollection = new SecurityCollection();

                    if (collection.getName() != null) {
                        tomcatSecCollection.setName(collection.getName());
                    }
                    if (collection.getDescription() != null) {
                        tomcatSecCollection.setDescription(collection.getDescription());
                    }

                    for (String pattern : collection.getPatterns()) {
                        tomcatSecCollection.addPattern(pattern);
                    }

                    for (String method : collection.getMethods()) {
                        tomcatSecCollection.addMethod(method);
                    }

                    for (String method : collection.getOmittedMethods()) {
                        tomcatSecCollection.addOmittedMethod(method);
                    }

                    tomcatConstraint.addCollection(tomcatSecCollection);
                }

                context.addConstraint(tomcatConstraint);
            }

            context.addParameter("keycloak.config.resolver", KeycloakSpringBootConfigResolver.class.getName());
        }
    }
}
