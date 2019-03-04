package com.github.catalin.cretu.conference;

import com.github.catalin.cretu.conference.togglz.FeatureRepository;
import com.github.catalin.cretu.conference.togglz.FeatureService;
import com.github.catalin.cretu.conference.togglz.FeatureToggle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class Fixtures {

    public static FeatureService featureService(final FeatureToggle featureToggle, final boolean active) {
        Map<FeatureToggle, Boolean> stateByFeatureToggle = new EnumMap<>(FeatureToggle.class);
        stateByFeatureToggle.put(featureToggle, active);

        return new FeatureService(new FeatureRepository() {

            @Override
            public Map<FeatureToggle, Boolean> findAll() {
                return stateByFeatureToggle;
            }

            @Override
            public Optional<FeatureToggle> findByName(final String featureName) {
                return stateByFeatureToggle.entrySet().stream()
                        .filter(entry -> entry.getKey().name().equalsIgnoreCase(featureName))
                        .map(Entry::getKey)
                        .findFirst();
            }

            @Override
            public void enable(final FeatureToggle featureToggle) {
                stateByFeatureToggle.put(featureToggle, true);
            }

            @Override
            public void disable(final FeatureToggle featureToggle) {
                stateByFeatureToggle.put(featureToggle, false);
            }
        });
    }
}
