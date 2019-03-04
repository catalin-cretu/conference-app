package com.github.catalin.cretu.conference.togglz;

import com.github.catalin.cretu.conference.api.ApiResponse;
import com.github.catalin.cretu.conference.api.feature.FeatureResponse;
import com.github.catalin.cretu.conference.api.feature.FeatureView;
import com.github.catalin.cretu.conference.path.api;
import com.github.catalin.cretu.conference.path.api.PathVars;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@ConditionalOnProperty(name = "togglz.enabled", havingValue = "true")
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(final FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping(path = api.Togglz)
    public ApiResponse<FeatureResponse> findAll() {
        log.debug("Find all features");

        var features = featureService.findAll().entrySet()
                .stream()
                .map(featureEntry -> new FeatureView(
                        featureEntry.getKey().name(),
                        featureEntry.getValue()))
                .collect(toSet());

        return ApiResponse.ok(new FeatureResponse(features));
    }

    @GetMapping(path = api.togglz.enable.byFeatureName)
    public ApiResponse<FeatureResponse> enableFeatureByName(
            @PathVariable(PathVars.featureName) final String featureName) {

        featureService.enable(featureName);

        return findAll();
    }

    @GetMapping(path = api.togglz.disable.byFeatureName)
    public ApiResponse<FeatureResponse> disableFeatureByName(
            @PathVariable(PathVars.featureName) final String featureName) {

        featureService.disable(featureName);

        return findAll();
    }
}