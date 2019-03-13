package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.fixture.Populated;
import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.github.catalin.cretu.conference.fixture.Fixtures.eventService;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class EventServiceTest {

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("Returns all events")
        void returnsAll() {
            var eventService = eventService(
                    Populated.event("E1").build(),
                    Populated.event("E2").build());

            assertThat(eventService.findAll())
                    .extracting(Event::getTitle)
                    .containsExactlyInAnyOrder("E1", "E2");
        }

        @Test
        @DisplayName("When no events - Returns empty")
        void returnsEmpty() {
            var eventService = eventService();

            assertThat(eventService.findAll())
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("createEvent")
    class Create {

        @Test
        @DisplayName("Returns created event")
        void returnsEvent() {
            var eventService = eventService();

            var createdEventResult = eventService.createEvent(
                    Event.builder()
                            .title("presentation")
                            .description("powerpoint")
                            .location("A1")
                            .period(Period.builder()
                                    .startDateTime(LocalDateTime.of(2019, 8, 7, 6, 5, 4))
                                    .endDateTime(LocalDateTime.of(2019, 8, 7, 7, 5, 4))
                                    .build())
                            .author(Author.builder()
                                    .name("Joe")
                                    .companyName("Org")
                                    .jobTitle("dev")
                                    .build())
                            .build());

            assertThat(createdEventResult.get())
                    .extracting(Event::getTitle, Event::getDescription, Event::getLocation)
                    .containsSequence("presentation", "powerpoint", "A1");

            assertThat(createdEventResult.get().getPeriod())
                    .extracting(Period::getStartDateTime, Period::getEndDateTime)
                    .containsSequence(
                            LocalDateTime.of(2019, 8, 7, 6, 5, 4),
                            LocalDateTime.of(2019, 8, 7, 7, 5, 4));

            assertThat(createdEventResult.get().getAuthor())
                    .extracting(Author::getName, Author::getCompanyName, Author::getJobTitle)
                    .containsSequence("Joe", "Org", "dev");
        }

        @Test
        @DisplayName("Saves event")
        void savesEvent() {
            var eventService = eventService();

            eventService.createEvent(
                    Populated.event("micro").build());

            assertThat(eventService.findAll())
                    .filteredOn(event -> "micro".equals(event.getTitle()))
                    .first()
                    .isNotNull();
        }

        @Nested
        @DisplayName("Returns error result")
        class ErrorResults {

            @Test
            @DisplayName("When event already exists")
            void eventExists() {
                var eventService = eventService();

                Result<Event> firstEvent = eventService.createEvent(Populated.event()
                        .author(Author.builder()
                                .name("Bob0")
                                .jobTitle("dev")
                                .companyName("JCN")
                                .build())
                        .period(Period.builder()
                                .startDateTime(LocalDateTime.of(2001, 1, 1, 11, 0))
                                .endDateTime(LocalDateTime.of(2001, 1, 1, 12, 0))
                                .build())
                        .build());
                assertThat(firstEvent.hasErrors()).isFalse();

                Event event = Populated.event()
                        .author(Author.builder()
                                .name("Bob")
                                .jobTitle("dev")
                                .companyName("JCN")
                                .build())
                        .period(Period.builder()
                                .startDateTime(LocalDateTime.of(2001, 1, 1, 11, 0))
                                .endDateTime(LocalDateTime.of(2001, 1, 1, 12, 0))
                                .build())
                        .build();
                Result<Event> secondEvent = eventService.createEvent(event);
                assertThat(secondEvent.hasErrors()).isFalse();

                Result<Event> duplicateEvent = eventService.createEvent(event);
                assertThat(duplicateEvent.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsSequence(tuple(
                                "DUPLICATE_EVENT",
                                "Cannot create duplicate event with author Bob (dev at JCN) " +
                                        "starting at 2001-01-01T11:00 and ending at 2001-01-01T12:00"));
            }

            @Test
            @DisplayName("When missing event details")
            void missingDetails() {
                var eventService = eventService();

                Result<Event> nullErrorResult = eventService.createEvent(
                        Populated.event(null)
                                .description(null)
                                .location(null)
                                .build());

                assertThat(nullErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("title", "title must not be blank"),
                                tuple("description", "description must not be blank"),
                                tuple("location", "location must not be blank"));

                Result<Event> emptyErrorResult = eventService.createEvent(
                        Populated.event(EMPTY)
                                .description(EMPTY)
                                .location(EMPTY)
                                .build());

                assertThat(emptyErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("title", "title must not be blank"),
                                tuple("description", "description must not be blank"),
                                tuple("location", "location must not be blank"));
            }

            @Test
            @DisplayName("When missing author or period")
            void missingAuthorPeriod() {
                var eventService = eventService();

                Result<Event> nullErrorResult = eventService.createEvent(
                        Populated.event()
                                .period(null)
                                .author(null)
                                .build());

                assertThat(nullErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("startDateTime", "period start date and time must not be blank"),
                                tuple("endDateTime", "period end date and time must not be blank"),
                                tuple("name", "author name must not be blank"),
                                tuple("jobTitle", "author job title must not be blank"),
                                tuple("companyName", "author company name must not be blank"));
            }

            @Test
            @DisplayName("When missing period details")
            void missingPeriod() {
                var eventService = eventService();

                Result<Event> nullErrorResult = eventService.createEvent(
                        Populated.event()
                                .period(Period.builder()
                                        .startDateTime(null)
                                        .endDateTime(null)
                                        .build())
                                .build());

                assertThat(nullErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("startDateTime", "period start date and time must not be blank"),
                                tuple("endDateTime", "period end date and time must not be blank"));
            }

            @Test
            @DisplayName("When start is after end date and time")
            void startAfterEndPeriod() {
                var eventService = eventService();

                Result<Event> nullErrorResult = eventService.createEvent(
                        Populated.event()
                                .period(Period.builder()
                                        .startDateTime(LocalDateTime.of(2000, 1, 1, 10, 1, 0))
                                        .endDateTime(LocalDateTime.of(2000, 1, 1, 10, 0, 0))
                                        .build())
                                .build());

                assertThat(nullErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("startDateTime", "period start date and time must be after end date and time"));
            }

            @Test
            @DisplayName("When missing author details")
            void missingAuthor() {
                var eventService = eventService();

                Result<Event> nullErrorResult = eventService.createEvent(
                        Populated.event()
                                .author(Author.builder()
                                        .name(null)
                                        .jobTitle(null)
                                        .companyName(null)
                                        .build())
                                .build());

                assertThat(nullErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("name", "author name must not be blank"),
                                tuple("jobTitle", "author job title must not be blank"),
                                tuple("companyName", "author company name must not be blank"));

                Result<Event> emptyErrorResult = eventService.createEvent(
                        Populated.event()
                                .author(Author.builder()
                                        .name(EMPTY)
                                        .jobTitle(EMPTY)
                                        .companyName(EMPTY)
                                        .build())
                                .build());

                assertThat(emptyErrorResult.getErrors())
                        .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                        .containsExactlyInAnyOrder(
                                tuple("name", "author name must not be blank"),
                                tuple("jobTitle", "author job title must not be blank"),
                                tuple("companyName", "author company name must not be blank"));
            }
        }
    }
}