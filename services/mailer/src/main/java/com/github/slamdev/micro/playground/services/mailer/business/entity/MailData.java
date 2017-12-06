package com.github.slamdev.micro.playground.services.mailer.business.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@SecondaryTable(name = "mail_data_from_person")
public class MailData {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean html;

    @Column(nullable = false)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(table = "mail_data_from_person", nullable = false)),
            @AttributeOverride(name = "name", column = @Column(table = "mail_data_from_person"))
    })
    private Person fromPerson;

    @ElementCollection
    private List<Person> carbonCopies;

    @ElementCollection
    private List<Person> blindCarbonCopies;

    @NonNull
    @ElementCollection
    private List<Person> toPersons;

    @ElementCollection
    private List<Attachment> attachments;

    private String error;

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

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Person {

        @Column(nullable = false)
        private String email;

        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Attachment {

        @Column(nullable = false)
        private String name;

        @Transient
        private byte[] content;
    }
}
