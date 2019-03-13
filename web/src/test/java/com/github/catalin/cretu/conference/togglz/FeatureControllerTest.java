package com.github.catalin.cretu.conference.togglz;

import com.github.catalin.cretu.conference.path.api;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.catalin.cretu.conference.togglz.FeatureToggle.EVENTS;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FeatureTestExtension
@DisplayName(api.Togglz)
class FeatureControllerTest {

    private final MockMvc mockMvc;
    private final FeatureService featureService;

    @Autowired
    FeatureControllerTest(final MockMvc mockMvc, final FeatureService featureService) {
        this.mockMvc = mockMvc;
        this.featureService = featureService;
    }

    @Test
    @DisplayName("GET  " + api.Togglz)
    void findAll() throws Exception {
        featureService.enable(EVENTS.name());

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
        featureService.disable(EVENTS.name());

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
        featureService.enable(EVENTS.name());

        mockMvc.perform(
                get(api.togglz.disable.byFeatureName, EVENTS.name())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.features.[*][?(@['name']=='" + EVENTS.name() + "')].active",
                        hasItem(false)));
    }
}