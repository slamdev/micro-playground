package com.github.slamdev.micro.playground.services.authenticator.business.entity;

import com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Value
@Builder
@EqualsAndHashCode(of = "id")
@Document
public class User {

    @Id
    String id;

    @NonNull
    @Indexed(unique = true)
    String email;

    @NonNull
    String password;

    @Indexed
    @NonNull
    Role role;

    @CreatedDate
    Instant created;

    @CreatedBy
    String createdBy;

    @LastModifiedDate
    Instant lastModified;

    @LastModifiedBy
    String lastModifiedBy;
}
