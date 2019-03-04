package com.github.catalin.cretu.conference.togglz;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum FeatureToggle implements Feature {

    NOOP,

    @Label("[EVENTS] GH-3: Create Events")
    EVENTS;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
