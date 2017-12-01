package com.github.slamdev.micro.playground.libs.authentication.client;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleValue {

    public static final String USER_ROLE = "ROLE_USER";
    public static final String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Getter
    public enum Role {

        USER(USER_ROLE), ADMIN(ADMIN_ROLE), ANONYMOUS(ANONYMOUS_ROLE);

        private final String name;

        Role(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
