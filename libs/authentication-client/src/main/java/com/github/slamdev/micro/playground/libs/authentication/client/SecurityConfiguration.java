package com.github.slamdev.micro.playground.libs.authentication.client;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import javax.servlet.Filter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Import(JwtAuthenticationProvider.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public SecurityConfiguration() {
        super(true);
        setTrustResolver(new AuthenticationTrustResolver() {
            @Override
            public boolean isAnonymous(Authentication authentication) {
                return false;
            }

            @Override
            public boolean isRememberMe(Authentication authentication) {
                return false;
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(new WebAsyncManagerIntegrationFilter())
                .addFilterBefore(jwtAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .securityContext()
                .and().anonymous()
                .and().servletApi()
                .and().exceptionHandling()
                .and().headers()
                .and().sessionManagement().sessionCreationPolicy(STATELESS)
        ;
    }

    @SneakyThrows
    private Filter jwtAuthenticationFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
}
