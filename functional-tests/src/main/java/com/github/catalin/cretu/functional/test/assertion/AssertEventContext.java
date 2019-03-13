package com.github.catalin.cretu.functional.test.assertion;

import com.github.catalin.cretu.conference.api.event.CreateEventRequest;
import com.github.catalin.cretu.conference.client.ConferenceClient;
import org.assertj.core.api.SoftAssertions;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class AssertEventContext {

    final SoftAssertions soft = new SoftAssertions();
    final Set<CreateEventRequest> expectedEvents = new HashSet<>();

    final ConferenceClient conferenceClient;

    String featureName;

    public AssertEventContext(final ConferenceClient conferenceClient) {
        this.conferenceClient = conferenceClient;
    }

    void assertAll() {
        soft.assertAll();
    }
}
