package com.github.catalin.cretu.conference.fixture;

import java.time.LocalDateTime;

import static com.github.catalin.cretu.conference.event.JpaEntity.Event;

public class Populated {

    public static Event.EventBuilder event() {
        return event("E1");
    }

    public static Event.EventBuilder event(final String name) {
        return Event.builder()
                .title(name)
                .description(name + " description")
                .startDateTime(LocalDateTime.of(2000, 1, 1, 10, 0, 0))
                .endDateTime(LocalDateTime.of(2000, 1, 1, 11, 0, 0))
                .name("Bob")
                .jobTitle("senior dev")
                .companyName("Acme Inc.");
    }
}
