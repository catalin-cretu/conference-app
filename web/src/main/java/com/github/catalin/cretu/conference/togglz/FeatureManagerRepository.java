package com.github.catalin.cretu.conference.togglz;

import lombok.extern.slf4j.Slf4j;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class FeatureManagerRepository implements FeatureRepository {

    private FeatureManager featureManager;

    FeatureManagerRepository(final FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @Override
    public Map<FeatureToggle, Boolean> findAll() {
        return featureManager.getFeatures()
                .stream()
                .map(feature -> FeatureToggle.valueOf(feature.name()))
                .collect(toMap(
                        feature -> feature,
                        featureManager::isActive));
    }

    @Override
    public Optional<FeatureToggle> findByName(final String featureName) {
        return Arrays.stream(FeatureToggle.values())
                .filter(featureToggle -> featureToggle.name().equalsIgnoreCase(featureName))
                .findFirst();
    }

    @Override
    public void enable(final FeatureToggle featureToggle) {
        featureManager.setFeatureState(
                new FeatureState(featureToggle, true));
    }

    @Override
    public void disable(final FeatureToggle featureToggle) {
        featureManager.setFeatureState(
                new FeatureState(featureToggle, false));
    }
}