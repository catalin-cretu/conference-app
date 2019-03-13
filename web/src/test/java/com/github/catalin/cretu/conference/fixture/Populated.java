package com.github.catalin.cretu.conference.fixture;

import com.github.catalin.cretu.conference.api.event.AuthorView;
import com.github.catalin.cretu.conference.api.event.CreateEventRequest;
import com.github.catalin.cretu.conference.api.event.PeriodView;
import com.github.catalin.cretu.conference.event.JpaEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class Populated {

    public static JpaEntity.Event.EventBuilder event() {
        return JpaEntity.Event.builder()
                .id(33L)
                .title("Exp")
                .description("experience")
                .location("RPG")
                .startDateTime(LocalDateTime.of(2020, 10, 9, 8, 7, 6))
                .endDateTime(LocalDateTime.of(2020, 10, 9, 8, 17, 6))
                .authorName("Jeane")
                .authorJobTitle("engineer")
                .authorCompanyName("Acme");
    }

    public static CreateEventRequest.CreateEventRequestBuilder createEventRequest() {
        return CreateEventRequest.builder()
                .title("New title")
                .description("new description")
                .location("Room-N")
                .period(periodView().build())
                .author(authorView().build());
    }

    private static PeriodView.PeriodViewBuilder periodView() {
        return PeriodView.builder()
                .startDateTime(LocalDateTime.of(2020, 10, 9, 8, 7, 6))
                .endDateTime(LocalDateTime.of(2020, 10, 9, 8, 17, 6));
    }

    private static AuthorView.AuthorViewBuilder authorView() {
        return AuthorView.builder()
                .name("Jenn")
                .jobTitle("QA")
                .companyName("QA Inc");
    }
}
