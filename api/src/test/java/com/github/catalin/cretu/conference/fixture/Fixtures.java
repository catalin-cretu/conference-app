package com.github.catalin.cretu.conference.fixture;

import com.github.catalin.cretu.conference.event.Event;
import com.github.catalin.cretu.conference.event.EventService;
import com.github.catalin.cretu.conference.togglz.FeatureService;
import com.github.catalin.cretu.conference.togglz.FeatureToggle;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fixtures {

    public static FeatureService featureService(final FeatureToggle featureToggle, final boolean active) {
        Map<FeatureToggle, Boolean> stateByFeatureToggle = new EnumMap<>(FeatureToggle.class);
        stateByFeatureToggle.put(featureToggle, active);

        return new FeatureService(new DefaultFeatureRepository(stateByFeatureToggle));
    }

    public static EventService eventService(Event... defaultEvents) {
        Set<Event> events = new HashSet<>(defaultEvents.length);
        events.addAll(Arrays.asList(defaultEvents));

        return new EventService(new DefaultEventRepository(events));
    }
}
