package com.github.catalin.cretu.functional.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.catalin.cretu.conference.client.ConferenceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class FunctionalTestConfig {

    private final String host;
    private final String port;

    public FunctionalTestConfig(
            @Value("${web.host}") final String host,
            @Value("${web.port}") final String port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    public ConferenceTestFactory conferenceTestFactory() {
        return new ConferenceTestFactory(conferenceClient());
    }

    @Bean
    public ConferenceClient conferenceClient() {
        var objectMapper = new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();

        return new ConferenceClient(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(5))
                        .build(),
                host + ":" + port,
                objectMapper,
                Duration.ofSeconds(5),
                "Content-Type", "application/json");
    }
}