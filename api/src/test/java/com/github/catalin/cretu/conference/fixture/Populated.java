package com.github.catalin.cretu.conference.fixture;

import com.github.catalin.cretu.conference.event.Author;
import com.github.catalin.cretu.conference.event.Event;
import com.github.catalin.cretu.conference.event.Period;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class Populated {

    public static Event.EventBuilder event() {
        return event("E1");
    }

    public static Event.EventBuilder event(final String title) {
        return Event.builder()
                .title(title)
                .description(title + " description")
                .location("1st room")
                .period(Period.builder()
                        .startDateTime(LocalDateTime.of(2000, 1, 1, 10, 0, 0))
                        .endDateTime(LocalDateTime.of(2000, 1, 1, 11, 0, 0))
                        .build())
                .author(author().build());
    }

    private static Author.AuthorBuilder author() {
        return Author.builder()
                .name("Bob")
                .jobTitle("senior dev")
                .companyName("Acme Inc.");
    }
}