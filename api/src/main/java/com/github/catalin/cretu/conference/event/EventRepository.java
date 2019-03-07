package com.github.catalin.cretu.conference.event;

import java.util.Set;

public interface EventRepository {

    Set<Event> findAll();

    Event save(final Event event);
}
