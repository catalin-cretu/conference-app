package com.github.catalin.cretu.functional.test.assertion;

import com.github.catalin.cretu.conference.api.event.AuthorView;
import com.github.catalin.cretu.conference.api.event.CreateEventRequest;
import com.github.catalin.cretu.conference.api.event.EventView;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@UtilityClass
public class AssertEvents {

    public static void exist(final AssertEventContext context) {
        try {
            SoftAssertions soft = context.soft;
            Set<CreateEventRequest> expectedEvents = context.expectedEvents;

            Set<EventView> actualEventResponses = findActualEvents(context);

            for (var eventResponse : actualEventResponses) {
                var optionalExpectedEvent = expectedEvents.stream()
                        .filter(event -> event.getTitle().equals(eventResponse.getTitle()))
                        .findFirst();

                optionalExpectedEvent.ifPresent(
                        expectedEvent -> assertEvent(soft, expectedEvent, eventResponse));
            }
        } catch (@SuppressWarnings("squid:S2142") IOException | InterruptedException e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    private static Set<EventView> findActualEvents(final AssertEventContext context)
            throws IOException, InterruptedException {
        var eventsResponse = context.conferenceClient.findAllEvents();

        assertThat(eventsResponse.statusCode())
                .isEqualTo(200);

        var expectedEventTitles = context.expectedEvents.stream()
                .map(CreateEventRequest::getTitle)
                .collect(toList());
        log.info("Assert [{}] events", expectedEventTitles.size());

        var events = eventsResponse.body().getEvents();
        var actualEventResponses = events.stream()
                .filter(event -> expectedEventTitles.contains(event.getTitle()))
                .collect(toSet());

        assertThat(actualEventResponses)
                .as("Could not find all actual events using expected titles %s", expectedEventTitles)
                .hasSize(expectedEventTitles.size());

        return actualEventResponses;
    }

    @SuppressWarnings("unchecked")
    private static void assertEvent(
            final SoftAssertions soft,
            final CreateEventRequest expectedEvent,
            final EventView actualEvent) {
        log.info("Check");
        soft.assertThat(actualEvent.getTitle())
                .isEqualTo(expectedEvent.getTitle());

        soft.assertThat(actualEvent.getDescription())
                .isEqualTo(expectedEvent.getDescription());

        var expectedAuthor = expectedEvent.getAuthor();
        soft.assertThat(actualEvent.getAuthor())
                .extracting(
                        AuthorView::getName,
                        AuthorView::getJobTitle,
                        AuthorView::getCompanyName)
                .containsSequence(
                        expectedAuthor.getName(),
                        expectedAuthor.getJobTitle(),
                        expectedAuthor.getCompanyName());
    }
}
