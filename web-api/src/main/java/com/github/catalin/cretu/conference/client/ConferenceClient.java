package com.github.catalin.cretu.conference.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.catalin.cretu.conference.api.event.CreateEventRequest;
import com.github.catalin.cretu.conference.api.event.CreateEventResponse;
import com.github.catalin.cretu.conference.api.event.EventsResponse;
import com.github.catalin.cretu.conference.api.feature.FeaturesResponse;
import com.github.catalin.cretu.conference.path.api;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Optional;

import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.util.Objects.requireNonNull;

public class ConferenceClient {

    private final HttpClient httpClient;
    private final String hostPort;
    private final ObjectMapper objectMapper;
    private final Duration requestTimeout;
    private final String[] headers;

    public ConferenceClient(
            final HttpClient httpClient,
            final String hostPort,
            final ObjectMapper objectMapper,
            final Duration requestTimeout,
            final String... headers) {
        this.httpClient = httpClient;
        this.hostPort = hostPort;
        this.objectMapper = objectMapper;
        this.requestTimeout = requestTimeout;
        this.headers = headers;
    }

    public static ConferenceClient defaultClient() {
        var objectMapper = new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();

        return new ConferenceClient(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(5))
                        .build(),
                "localhost:8080",
                objectMapper,
                Duration.ofSeconds(5),
                "Content-Type", "application/json");
    }

    public HttpResponse<FeaturesResponse> enableFeature(final String featureName)
            throws IOException, InterruptedException {

        var httpResponse = httpClient.send(
                toGetRequest(toURI(api.togglz.enableByFeature(featureName))),
                BodyHandlers.ofString());

        return new Response<>(httpResponse, FeaturesResponse.class);
    }

    public HttpResponse<CreateEventResponse> createEvent(final CreateEventRequest createEventRequest)
            throws IOException, InterruptedException {

        var httpResponse = httpClient.send(
                toPostRequest(createEventRequest, toURI(api.Events)),
                BodyHandlers.ofString());

        return new Response<>(httpResponse, CreateEventResponse.class);
    }

    public HttpResponse<EventsResponse> findAllEvents() throws IOException, InterruptedException {
        var httpResponse = httpClient.send(
                toGetRequest(toURI(api.Events)),
                BodyHandlers.ofString());
        return new Response<>(httpResponse, EventsResponse.class);
    }

    private URI toURI(final String resourceUrl) throws IOException {
        try {
            var host = hostPort.startsWith("http")
                    ? hostPort
                    : "http://" + hostPort;
            return new URI(host + resourceUrl);
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private HttpRequest toGetRequest(final URI restUri) {
        return HttpRequest.newBuilder()
                .timeout(requestTimeout)
                .headers(headers)
                .uri(restUri)
                .GET()
                .build();
    }

    private HttpRequest toPostRequest(final Object request, final URI restUri) throws JsonProcessingException {
        return HttpRequest.newBuilder()
                .timeout(requestTimeout)
                .headers(headers)
                .uri(restUri)
                .POST(ofByteArray(objectMapper.writeValueAsBytes(request)))
                .build();
    }

    private class Response<T> implements HttpResponse<T> {

        private final HttpResponse<String> httpResponse;
        private Class<T> responseType;

        private Response(final HttpResponse<String> httpResponse, final Class<T> responseType) {
            this.httpResponse = httpResponse;
            this.responseType = requireNonNull(responseType, "Response type cannot be null");
        }

        @Override
        public int statusCode() {
            return httpResponse.statusCode();
        }

        @Override
        public HttpRequest request() {
            return httpResponse.request();
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return httpResponse.headers();
        }

        @Override
        public T body() {
            try {
                if (responseType != null) {
                    return objectMapper.readValue(httpResponse.body(), responseType);
                }
                return null;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return httpResponse.sslSession();
        }

        @Override
        public URI uri() {
            return httpResponse.uri();
        }

        @Override
        public HttpClient.Version version() {
            return httpResponse.version();
        }
    }
}