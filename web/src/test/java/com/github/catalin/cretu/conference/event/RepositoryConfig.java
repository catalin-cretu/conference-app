package com.github.catalin.cretu.conference.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;

@Configuration
public class RepositoryConfig {

    @Bean
    @Profile("events-controller")
    public EventJpaRepository eventJpaRepository() {
        return new InMemEventJpaRepository(new HashSet<>(), 0L);
    }
}
