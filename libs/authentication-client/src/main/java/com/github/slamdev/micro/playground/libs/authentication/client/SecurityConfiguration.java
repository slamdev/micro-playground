package com.github.slamdev.micro.playground.libs.authentication.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({
        JwtAuthenticationProvider.class,
        JwtAuthenticationFilter.class,
        JwtFactory.class
})
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        super(true);
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .securityContext()
                .and().anonymous()
                .and().servletApi()
                .and().exceptionHandling()
                .and().headers()
                .and().sessionManagement().sessionCreationPolicy(STATELESS)
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // This method is here with the @Bean annotation so that Spring can autowire it
        return super.authenticationManagerBean();
    }
}
