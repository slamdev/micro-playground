package com.github.slamdev.micro.playground.libs.authentication.client;

import lombok.Getter;
import lombok.ToString;

public interface RoleValue {

    String USER_ROLE = "ROLE_USER";
    String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";
    String ADMIN_ROLE = "ROLE_ADMIN";

    @Getter
    @ToString
    enum Role {

        USER(USER_ROLE), ADMIN(ADMIN_ROLE), ANONYMOUS(ANONYMOUS_ROLE);

        private final String name;

        Role(String name) {
            this.name = name;
        }
    }
}
