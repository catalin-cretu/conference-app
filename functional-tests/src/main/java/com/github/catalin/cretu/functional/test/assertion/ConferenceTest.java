package com.github.catalin.cretu.functional.test.assertion;

import com.github.catalin.cretu.conference.api.feature.FeatureView;
import com.github.catalin.cretu.conference.client.ConferenceClient;
import com.github.catalin.cretu.conference.togglz.FeatureToggle;
import com.github.catalin.cretu.functional.test.event.EventExample;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Slf4j
public class ConferenceTest {

    private static final int OK_STATUS_CODE = 200;

    private final AssertEventContext eventContext;
    private final ConferenceClient conferenceClient;

    public ConferenceTest(final ConferenceClient conferenceClient) {
        this.conferenceClient = conferenceClient;
        eventContext = new AssertEventContext(conferenceClient);
    }

    public void enableFeature(final FeatureToggle featureToggle) throws IOException, InterruptedException {
        log.info("Enable feature {}", featureToggle);

        eventContext.featureName = featureToggle.name();
        var featureResponse = conferenceClient.enableFeature(eventContext.featureName);

        assertThat(featureResponse.statusCode())
                .isEqualTo(OK_STATUS_CODE);
        assertThat(featureResponse.body().getFeatures())
                .extracting(FeatureView::getName, FeatureView::isActive)
                .containsSequence(
                        tuple(eventContext.featureName, true));
    }

    public ConferenceTest createEvent(
            final EventExample eventExample,
            final LocalDateTime startDateTime,
            final Duration duration)
            throws IOException, InterruptedException {
        var createEventRequest = eventExample.createEventRequest(startDateTime, duration);
        log.info("Create event: {}", createEventRequest.getTitle());

        var createEventResponse = conferenceClient.createEvent(createEventRequest);
        assertThat(createEventResponse.statusCode())
                .isEqualTo(200);

        eventContext.expectedEvents.add(createEventRequest);
        return this;
    }

    @SafeVarargs
    public final void eventAssertions(final Consumer<AssertEventContext>... eventAssertions) {
        for (var eventAssertion : eventAssertions) {
            eventAssertion.accept(eventContext);
        }
        eventContext.assertAll();
        eventContext.expectedEvents.clear();
    }

    public void cleanUp() throws IOException, InterruptedException {
        conferenceClient.disableFeature(eventContext.featureName);
    }
}
