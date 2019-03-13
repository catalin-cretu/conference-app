package com.github.catalin.cretu.conference.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public EventService eventService(final EventJpaRepository eventJpaRepository) {
        return new EventService(new DefaultEventRepository(eventJpaRepository));
    }
}