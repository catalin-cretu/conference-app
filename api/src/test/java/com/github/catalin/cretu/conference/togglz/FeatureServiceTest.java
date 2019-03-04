package com.github.catalin.cretu.conference.togglz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.catalin.cretu.conference.Fixtures.featureService;
import static com.github.catalin.cretu.conference.togglz.FeatureToggle.NOOP;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

class FeatureServiceTest {

    private FeatureService featureService = featureService(NOOP, true);

    @Test
    @DisplayName("findAll - Returns all states")
    void findAll() {
        assertThat(featureService.findAll())
                .contains(entry(NOOP, true));
    }

    @Test
    @DisplayName("enableFeature - Existing feature - Returns all states")
    void enableFeature() {
        featureService = featureService(NOOP, false);

        assertThat(featureService.enable(NOOP.name()))
                .contains(entry(NOOP, true));
    }

    @Test
    @DisplayName("enableFeature - Nonexistent feature - Returns all states")
    void enableFeature_NonExistentFeature() {
        featureService = featureService(NOOP, false);

        assertThat(featureService.enable("NON_EXISTENT"))
                .doesNotContainValue(true);
    }

    @Test
    @DisplayName("disableFeature - Existing feature - Returns all states")
    void disableFeature() {
        featureService = featureService(NOOP, true);

        assertThat(featureService.disable(NOOP.name()))
                .contains(entry(NOOP, false));
    }

    @Test
    @DisplayName("disableFeature - Nonexistent feature - Returns all states")
    void disableFeature_NonExistentFeature() {
        featureService = featureService(NOOP, true);

        assertThat(featureService.disable("NON_EXISTENT"))
                .doesNotContainValue(false);
    }
}