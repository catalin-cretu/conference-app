package com.github.catalin.cretu.functional.test;

import com.github.catalin.cretu.conference.client.ConferenceClient;
import com.github.catalin.cretu.functional.test.assertion.ConferenceTest;

class ConferenceTestFactory {

    private ConferenceClient conferenceClient;

    ConferenceTestFactory(final ConferenceClient conferenceClient) {
        this.conferenceClient = conferenceClient;
    }

    ConferenceTest conferenceTest() {
        return new ConferenceTest(conferenceClient);
    }
}
