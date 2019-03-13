package com.github.catalin.cretu.conference.fixture;

import com.github.catalin.cretu.conference.togglz.FeatureRepository;
import com.github.catalin.cretu.conference.togglz.FeatureToggle;

import java.util.Map;
import java.util.Optional;

class InMemFeatureRepository implements FeatureRepository {

    private Map<FeatureToggle, Boolean> stateByFeatureToggle;

    InMemFeatureRepository(final Map<FeatureToggle, Boolean> stateByFeatureToggle) {
        this.stateByFeatureToggle = stateByFeatureToggle;
    }

    @Override
    public Map<FeatureToggle, Boolean> findAll() {
        return stateByFeatureToggle;
    }

    @Override
    public Optional<FeatureToggle> findByName(final String featureName) {
        return stateByFeatureToggle.entrySet().stream()
                .filter(entry -> entry.getKey().name().equalsIgnoreCase(featureName))
                .map(Map.Entry::getKey)
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
}
