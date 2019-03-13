package com.github.catalin.cretu.functional.test.event;

import com.github.catalin.cretu.conference.api.event.AuthorView;
import com.github.catalin.cretu.conference.api.event.CreateEventRequest;
import com.github.catalin.cretu.conference.api.event.PeriodView;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Getter
public enum EventExample {

    DISTRIBUTED_SYSTEMS(
            "Democratizing Distributed Systems: Kubernetes, Brigade, Metaparticle and Beyond",
            "Effectenbeurszaal",
            "Simply put despite all of the advances in cloud over the last decade, " +
                    "it is too hard to build a reliable, cloud-native application",
            AuthorView.builder()
                    .name("Brendan Burns")
                    .jobTitle("Co-founder of Kubernetes & Distinguished Engineer")
                    .companyName("Microsoft")
                    .build()),
    EVENT_DRIVEN_MICROSERVICES(
            "Pragmatic Event-Driven Microservices",
            "Graanbeurszaal",
            "Allard Buijze outlines an evolutionary approach to microservices, " +
                    "which allows teams to focus on functionality first " +
                    "while keeping the ability to scale out and distribute components when necessary.",
            AuthorView.builder()
                    .name("Allard Buijze")
                    .jobTitle("Creator of Axon Framework")
                    .companyName("Allard Buijze")
                    .build()),
    SCALING_SLACK(
            "Scaling Slack",
            "Veilingzaal",
            "Scaling a connection-oriented service presents challenges " +
                    "that differ from those of a typical, request-based web service",
            AuthorView.builder()
                    .name("Keith Adams")
                    .jobTitle("Chief Architect ")
                    .companyName("Slack")
                    .build());

    private final String title;
    private final String description;
    private final String location;
    private final AuthorView author;

    EventExample(
            final String title,
            final String location,
            final String description,
            final AuthorView author) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.author = author;
    }

    public CreateEventRequest createEventRequest(final LocalDateTime startDateTime, final Duration duration) {
        return CreateEventRequest.builder()
                .period(PeriodView.builder()
                        .startDateTime(startDateTime)
                        .endDateTime(startDateTime.plus(duration))
                        .build())
                .title(randomAlphabetic(30) + " :: " + getTitle())
                .location(getLocation())
                .description(getDescription())
                .author(author)
                .build();
    }
}