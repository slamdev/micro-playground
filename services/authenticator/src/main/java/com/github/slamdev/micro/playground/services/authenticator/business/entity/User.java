package com.github.slamdev.micro.playground.services.authenticator.business.entity;

import com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user_data" /* postgres disallow to have table named `user` */, indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "role")
})
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(STRING)
    @ElementCollection
    private List<Role> role;

    @CreatedDate
    @Column(nullable = false)
    private Instant created;

    @CreatedBy
    @Column(nullable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModified;

    @LastModifiedBy
    @Column(nullable = false)
    private String lastModifiedBy;
}
