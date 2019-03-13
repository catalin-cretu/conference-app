package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.ErrorResult;
import com.github.catalin.cretu.conference.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static java.util.stream.Collectors.joining;

@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    Set<Event> findAll() {
        var events = eventRepository.findAll();

        log.debug("Found {} events", events.size());
        return events;
    }

    Result<Event> createEvent(final Event event) {
        if (eventRepository.exists(event)) {
            return toDuplicateEventResult(event);
        }
        var errors = event.validate();

        if (!errors.isEmpty()) {
            logErrors(errors);
            return Result.errors(errors);
        }
        var period = event.getPeriod();
        log.info("Create event [{}] with period [{} - {}]",
                event.getTitle(), period.getStartDateTime(), period.getEndDateTime());

        var createdEvent = eventRepository.save(event);
        return Result.ok(createdEvent);
    }

    private static Result<Event> toDuplicateEventResult(final Event event) {
        var author = event.getAuthor();
        var authorMsg = author.getName() + " (" + author.getJobTitle() + " at " + author.getCompanyName() + ")";
        var period = event.getPeriod();

        var errorMessage = "Cannot create duplicate event with author " + authorMsg
                + " starting at " + period.getStartDateTime()
                + " and ending at " + period.getEndDateTime();

        log.warn(errorMessage);
        return Result.error("DUPLICATE_EVENT", errorMessage);
    }

    private void logErrors(final Set<ErrorResult> errors) {
        log.warn("Errors:\n{}", errors.stream()
                .map(error -> "[" + error.getCode() + "] " + error.getMessage())
                .collect(joining("\n")));
    }
}