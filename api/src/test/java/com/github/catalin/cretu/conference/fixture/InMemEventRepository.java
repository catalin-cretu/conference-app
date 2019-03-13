package com.github.catalin.cretu.conference.fixture;

import com.github.catalin.cretu.conference.event.Event;
import com.github.catalin.cretu.conference.event.EventRepository;

import java.util.Set;

class InMemEventRepository implements EventRepository {

    private Set<Event> events;

    InMemEventRepository(final Set<Event> defaultEvents) {
        this.events = defaultEvents;
    }

    @Override
    public Set<Event> findAll() {
        return events;
    }

    @Override
    public Event save(final Event event) {
        events.add(event);
        return event;
    }

    @Override
    public boolean exists(final Event event) {
        return events.contains(event);
    }
}