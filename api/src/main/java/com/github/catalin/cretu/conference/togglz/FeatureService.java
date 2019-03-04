package com.github.catalin.cretu.conference.togglz;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FeatureService {

    private final FeatureRepository featureRepository;

    public FeatureService(final FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    public Map<FeatureToggle, Boolean> findAll() {
        return featureRepository.findAll();
    }

    Map<FeatureToggle, Boolean> enable(final String featureName) {
        var optionalFeature = featureRepository.findByName(featureName);

        if (optionalFeature.isPresent()) {
            enableFeature(optionalFeature.get());
        } else {
            logMissing(featureName);
        }
        return findAll();
    }

    private void enableFeature(final FeatureToggle featureToggle) {
        log.info("Enable feature [{}]", featureToggle);

        featureRepository.enable(featureToggle);
    }

    Map<FeatureToggle, Boolean> disable(final String featureName) {
        var optionalFeature = featureRepository.findByName(featureName);

        if (optionalFeature.isPresent()) {
            disableFeature(optionalFeature.get());
        } else {
            logMissing(featureName);
        }
        return findAll();
    }

    private void disableFeature(final FeatureToggle featureToggle) {
        log.info("Disable feature [{}]", featureToggle);

        featureRepository.disable(featureToggle);
    }

    private void logMissing(final String featureName) {
        log.warn("Feature [{}] is missing", featureName);
    }
}
