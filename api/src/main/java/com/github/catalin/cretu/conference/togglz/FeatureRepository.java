package com.github.catalin.cretu.conference.togglz;

import java.util.Map;
import java.util.Optional;

public interface FeatureRepository {

    Map<FeatureToggle, Boolean> findAll();

    Optional<FeatureToggle> findByName(final String featureName);

    void enable(final FeatureToggle featureName);

    void disable(final FeatureToggle featureName);
}
