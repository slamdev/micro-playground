package com.github.slamdev.micro.playground.services.mailer.business.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document
public class MailData {

    @Id
    private String id;

    @NonNull
    private String subject;

    @NonNull
    private String text;

    private boolean html;

    @NonNull
    private Person fromPerson;

    private List<Person> carbonCopies;

    private List<Person> blindCarbonCopies;

    @NonNull
    private List<Person> toPersons;

    private List<Attachment> attachments;

    private String error;

    @CreatedDate
    private Instant created;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant lastModified;

    @LastModifiedBy
    private String lastModifiedBy;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {

        @NonNull
        private String email;

        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attachment {

        @NonNull
        private String name;

        @NonNull
        private byte[] content;
    }
}
