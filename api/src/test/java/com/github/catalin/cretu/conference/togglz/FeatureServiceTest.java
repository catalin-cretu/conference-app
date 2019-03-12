package com.github.catalin.cretu.conference.togglz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.catalin.cretu.conference.fixture.Fixtures.featureService;
import static com.github.catalin.cretu.conference.togglz.FeatureToggle.NOOP;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

class FeatureServiceTest {

    @Test
    @DisplayName("findAll - Returns all states")
    void findAll() {
        var featureService = featureService(NOOP, true);

        assertThat(featureService.findAll())
                .contains(entry(NOOP, true));
    }

    @Nested
    @DisplayName("enableFeature")
    class EnableFeature {

        @Test
        @DisplayName("With existing feature - Returns all features")
        void existingFeature() {
            var featureService = featureService(NOOP, false);

            assertThat(featureService.enable(NOOP.name()))
                    .contains(entry(NOOP, true));
        }

        @Test
        @DisplayName("With missing feature - Returns all features")
        void missingFeature() {
            var featureService = featureService(NOOP, false);

            assertThat(featureService.enable("NON_EXISTENT"))
                    .doesNotContainValue(true);
        }
    }

    @Nested
    @DisplayName("disableFeature")
    class DisableFeature {

        @Test
        @DisplayName("With existing feature - Returns all features")
        void existingFeature() {
            var featureService = featureService(NOOP, true);

            assertThat(featureService.disable(NOOP.name()))
                    .contains(entry(NOOP, false));
        }

        @Test
        @DisplayName("With missing feature - Returns all features")
        void missingFeature() {
            var featureService = featureService(NOOP, true);

            assertThat(featureService.disable("NON_EXISTENT"))
                    .doesNotContainValue(false);
        }
    }
}