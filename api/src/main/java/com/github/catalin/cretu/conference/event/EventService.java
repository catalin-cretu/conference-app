package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

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
        var errors = event.validate();

        if (!errors.isEmpty()) {
            return Result.errors(errors);
        }
        var createdEvent = eventRepository.save(event);

        Period period = createdEvent.getPeriod();
        log.info("Created event [{}] with period [{} - {}]",
                createdEvent.getTitle(), period.getStartDateTime(), period.getEndDateTime());

        return Result.ok(createdEvent);
    }
}