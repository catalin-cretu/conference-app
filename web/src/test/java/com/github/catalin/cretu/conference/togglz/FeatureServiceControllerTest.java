package com.github.catalin.cretu.conference.togglz;

import com.github.catalin.cretu.conference.path.api;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

import static com.github.catalin.cretu.conference.togglz.FeatureToggle.EVENTS;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FeatureTestExtension
class FeatureServiceControllerTest {

    private final MockMvc mockMvc;
    private final FeatureManager featureManager;

    @Autowired
    FeatureServiceControllerTest(final MockMvc mockMvc, final FeatureManager featureManager) {
        this.mockMvc = mockMvc;
        this.featureManager = featureManager;
    }

    @Test
    @DisplayName("GET  " + api.Togglz)
    void findAll() throws Exception {
        featureManager.setFeatureState(new FeatureState(EVENTS, true));

        mockMvc.perform(
                get(api.Togglz)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.features.[*].active", hasItem(true)))
                .andExpect(jsonPath("$.features.[*].name", hasItem(EVENTS.name())));

    }

    @Test
    @DisplayName("GET  " + api.togglz.enable.byFeatureName)
    void enableFeatureByName() throws Exception {
        featureManager.setFeatureState(new FeatureState(EVENTS, false));

        mockMvc.perform(
                get(api.togglz.enable.byFeatureName, EVENTS.name())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.features.[*][?(@['name']=='" + EVENTS.name() + "')].active",
                        hasItem(true)));
    }

    @Test
    @DisplayName("GET  " + api.togglz.disable.byFeatureName)
    void disableFeatureByName() throws Exception {
        featureManager.setFeatureState(new FeatureState(EVENTS, true));

        mockMvc.perform(
                get(api.togglz.disable.byFeatureName, EVENTS.name())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.features.[*][?(@['name']=='" + EVENTS.name() + "')].active",
                        hasItem(false)));
    }
}