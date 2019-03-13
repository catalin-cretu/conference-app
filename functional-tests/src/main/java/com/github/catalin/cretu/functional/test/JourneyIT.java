package com.github.catalin.cretu.functional.test;

import com.github.catalin.cretu.functional.test.assertion.AssertEvents;
import com.github.catalin.cretu.functional.test.assertion.ConferenceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.github.catalin.cretu.conference.togglz.FeatureToggle.EVENTS;
import static com.github.catalin.cretu.functional.test.event.EventExample.DISTRIBUTED_SYSTEMS;
import static com.github.catalin.cretu.functional.test.event.EventExample.EVENT_DRIVEN_MICROSERVICES;
import static com.github.catalin.cretu.functional.test.event.EventExample.SCALING_SLACK;

@JourneyTestExtension
@SuppressWarnings("squid:S00112")
class JourneyIT {

    private final ConferenceTest conferenceTest;

    @Autowired
    JourneyIT(final ConferenceTestFactory conferenceTestFactory) {
        conferenceTest = conferenceTestFactory.conferenceTest();
    }

    @Test
    @Order(10)
    @DisplayName("Enable EVENTS feature")
    void enableFeature() throws Exception {
        conferenceTest.enableFeature(EVENTS);
    }

    @Test
    @Order(20)
    @DisplayName("Create Day 1 Events")
    void createDay1() throws Exception {
        conferenceTest
                .createEvent(
                        DISTRIBUTED_SYSTEMS,
                        LocalDateTime.of(2018, 3, 1, 11, 0),
                        Duration.ofMinutes(45))
                .createEvent(
                        EVENT_DRIVEN_MICROSERVICES,
                        LocalDateTime.of(2018, 3, 1, 12, 0),
                        Duration.ofMinutes(45));
    }

    @Test
    @Order(30)
    @DisplayName("Create Day 2 Events")
    void createDay2() throws Exception {
        conferenceTest
                .createEvent(
                        DISTRIBUTED_SYSTEMS,
                        LocalDateTime.of(2018, 3, 2, 11, 0),
                        Duration.ofMinutes(60))
                .createEvent(
                        SCALING_SLACK,
                        LocalDateTime.of(2018, 3, 2, 14, 0),
                        Duration.ofMinutes(45));
    }

    @Test
    @Order(40)
    @DisplayName("Assert expected events")
    void assertEvents() {
        conferenceTest.eventAssertions(
                AssertEvents::exist);
    }

    @Test
    @DisplayName("Clean up")
    void cleanUp() throws Exception {
        conferenceTest.cleanUp();
    }
}