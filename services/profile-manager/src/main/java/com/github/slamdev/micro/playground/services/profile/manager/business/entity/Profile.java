package com.github.slamdev.micro.playground.services.profile.manager.business.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(indexes = {
        @Index(columnList = "userId", unique = true)
})
public class Profile {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Instant birthDate;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Gender gender;

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

    public enum Gender {
        MALE, FEMALE;
    }
}
