package com.github.catalin.cretu.conference.togglz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

@Configuration
class FeatureConfig {

    @Bean
    FeatureService feature(final FeatureManager featureManager) {
        return new FeatureService(new FeatureManagerRepository(featureManager));
    }
}