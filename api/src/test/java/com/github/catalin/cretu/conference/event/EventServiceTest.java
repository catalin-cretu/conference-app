package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.fixture.Populated;
import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.github.catalin.cretu.conference.fixture.Fixtures.eventService;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class EventServiceTest {

    @Test
    @DisplayName("findAll - Returns all events")
    void findAll() {
        var eventService = eventService(
                Populated.event("E1").build(),
                Populated.event("E2").build());

        assertThat(eventService.findAll())
                .extracting(Event::getTitle)
                .containsExactlyInAnyOrder("E1", "E2");
    }

    @Test
    @DisplayName("findAll - No events - Returns empty")
    void findAll_Empty() {
        var eventService = eventService();

        assertThat(eventService.findAll())
                .isEmpty();
    }

    @Test
    @DisplayName("createEvent - Returns created event")
    void createEvent_ReturnsEvent() {
        var eventService = eventService();

        var createdEventResult = eventService.createEvent(
                Event.builder()
                        .title("presentation")
                        .description("powerpoint")
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
                .extracting(Event::getTitle, Event::getDescription)
                .containsSequence("presentation", "powerpoint");

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
    @DisplayName("createEvent - Returns created event")
    void createEvent_SavesEvent() {
        var eventService = eventService();

        eventService.createEvent(
                Populated.event("micro").build());

        assertThat(eventService.findAll())
                .filteredOn(event -> "micro".equals(event.getTitle()))
                .first()
                .isNotNull();
    }

    @Test
    @DisplayName("createEvent - Missing event details - Returns error result")
    void createEvent_NoDetails() {
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
    @DisplayName("createEvent - Missing period details - Returns error result")
    void createEvent_NoPeriod() {
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
                        tuple("startDateTime", "startDateTime must not be blank"),
                        tuple("endDateTime", "endDateTime must not be blank"));
    }

    @Test
    @DisplayName("createEvent - Missing author name - Returns error result")
    void createEvent_AuthorWithNoDetails() {
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