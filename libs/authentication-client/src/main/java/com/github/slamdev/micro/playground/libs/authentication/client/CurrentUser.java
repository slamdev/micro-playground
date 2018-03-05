package com.github.slamdev.micro.playground.libs.authentication.client;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role;
import static java.util.stream.Collectors.toList;

@Component
public class CurrentUser {

    public List<Role> roles() {
        return token().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .collect(toList());
    }

    public Optional<Long> id() {
        return jwt()
                .map(JwtAuthenticationToken::getPrincipal)
                .flatMap(p -> attribute(p, "userId"))
                .map(Long.class::cast);
    }

    private Optional<Object> attribute(OAuth2User user, String name) {
        return Optional.ofNullable(user.getAttributes().get(name));
    }

    private Optional<JwtAuthenticationToken> jwt() {
        Authentication token = token();
        if (token instanceof JwtAuthenticationToken) {
            return Optional.of((JwtAuthenticationToken) token);
        }
        return Optional.empty();
    }

    private Authentication token() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
