package com.github.slamdev.micro.playground.services.authenticator.business.entity;

import com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document
public class User {

    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String email;

    @NonNull
    private String password;

    @Indexed
    @NonNull
    private Role role;

    @CreatedDate
    private Instant created;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant lastModified;

    @LastModifiedBy
    private String lastModifiedBy;
}
