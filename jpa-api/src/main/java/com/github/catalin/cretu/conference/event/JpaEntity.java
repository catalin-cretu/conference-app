package com.github.catalin.cretu.conference.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@UtilityClass
public class JpaEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "EVENT")
    public static class Event {

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(name = "ID")
        private Long id;


        @Column(name = "START_DATE_TIME")
        private LocalDateTime startDateTime;

        @Column(name = "END_DATE_TIME")
        private LocalDateTime endDateTime;


        @Column(name = "TITLE")
        private String title;

        @Column(name = "DESCRIPTION")
        private String description;

        @Column(name = "LOCATION")
        private String location;


        @Column(name = "AUTHOR_NAME")
        private String authorName;

        @Column(name = "AUTHOR_JOB_TITLE")
        private String authorJobTitle;

        @Column(name = "AUTHOR_COMPANY_NAME")
        private String authorCompanyName;
    }
}
