package com.github.catalin.cretu.conference.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.catalin.cretu.conference.fixture.Populated;
import com.github.catalin.cretu.conference.path.api;
import com.github.catalin.cretu.conference.togglz.FeatureToggle;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;

import java.time.LocalDateTime;
import java.util.Set;

import static com.github.catalin.cretu.conference.togglz.FeatureToggle.EVENTS;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EventTestExtension
class EventsControllerTest {

    private final SoftAssertions soft = new SoftAssertions();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private InMemEventJpaRepository eventJpaRepository;

    @Nested
    @DisplayName("POST  " + api.Events)
    class Create {

        @Test
        @DisplayName("Returns Created Event ID")
        @AllEnabled(FeatureToggle.class)
        void create_ReturnsResponse() throws Exception {
            var createEventJsonRequest = objectMapper.createObjectNode()
                    .put("title", "Garden")
                    .put("description", "Garden of code")
                    .put("location", "outside");
            createEventJsonRequest.set("period", objectMapper.createObjectNode()
                    .put("startDateTime", "2000-01-01T10:00:00")
                    .put("endDateTime", "2000-01-01T11:00:00"));
            createEventJsonRequest.set("author", objectMapper.createObjectNode()
                    .put("name", "Jeane Doe")
                    .put("jobTitle", "dev")
                    .put("companyName", "Acme Inc"));

            mockMvc.perform(
                    post(api.Events)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createEventJsonRequest)))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.id").value(eventJpaRepository.getCurrentId()));
        }

        @Test
        @DisplayName("Saves Event")
        @AllEnabled(FeatureToggle.class)
        void create_SavesEvent() throws Exception {
            var createEventRequest = objectMapper.createObjectNode()
                    .put("title", "Garden")
                    .put("description", "Garden of code")
                    .put("location", "outside");
            createEventRequest.set("period", objectMapper.createObjectNode()
                    .put("startDateTime", "2000-01-01T10:00:00")
                    .put("endDateTime", "2000-01-01T11:00:00"));
            createEventRequest.set("author", objectMapper.createObjectNode()
                    .put("name", "Jeane Doe")
                    .put("jobTitle", "dev")
                    .put("companyName", "Acme Inc"));

            mockMvc.perform(
                    post(api.Events)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createEventRequest)))
                    .andExpect(status().isOk());

            @SuppressWarnings("OptionalGetWithoutIsPresent")
            JpaEntity.Event savedEvent = eventJpaRepository.findById(eventJpaRepository.getCurrentId()).get();

            soft.assertThat(savedEvent.getTitle())
                    .isEqualTo("Garden");
            soft.assertThat(savedEvent.getDescription())
                    .isEqualTo("Garden of code");
            soft.assertThat(savedEvent.getLocation())
                    .isEqualTo("outside");
            soft.assertThat(savedEvent.getStartDateTime())
                    .isEqualTo(LocalDateTime.of(2000, 1, 1, 10, 0, 0));
            soft.assertThat(savedEvent.getEndDateTime())
                    .isEqualTo(LocalDateTime.of(2000, 1, 1, 11, 0, 0));
            soft.assertThat(savedEvent.getAuthorName())
                    .isEqualTo("Jeane Doe");
            soft.assertThat(savedEvent.getAuthorJobTitle())
                    .isEqualTo("dev");
            soft.assertThat(savedEvent.getAuthorCompanyName())
                    .isEqualTo("Acme Inc");

            soft.assertAll();
        }

        @Test
        @DisplayName("With inactive feature - Returns inactive error")
        @AllDisabled(FeatureToggle.class)
        void create_FeatureError() throws Exception {
            mockMvc.perform(
                    post(api.Events)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    Populated.createEventRequest().build())))

                    .andExpect(status().isInternalServerError())

                    .andExpect(jsonPath("$.errors.[0].code", containsString("INACTIVE")))
                    .andExpect(jsonPath("$.errors.[0].message", containsString(EVENTS.name())));
        }

        @Test
        @DisplayName("With result errors - Returns result errors")
        @AllEnabled(FeatureToggle.class)
        void create_ResultError() throws Exception {
            mockMvc.perform(
                    post(api.Events)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    Populated.createEventRequest()
                                            .title(null)
                                            .build())))

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.errors.[0].code", containsString("title")))
                    .andExpect(jsonPath("$.errors.[0].message", containsString("not be blank")));

            mockMvc.perform(
                    post(api.Events)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    Populated.createEventRequest()
                                            .author(null)
                                            .period(null)
                                            .build())))

                    .andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.errors", hasSize(5)));
        }
    }

    @Nested
    @DisplayName("GET  " + api.Events)
    class FindAll {

        @Test
        @DisplayName("With no events - Returns empty events")
        @AllEnabled(FeatureToggle.class)
        void findAll_Empty() throws Exception {
            eventJpaRepository.deleteAll();

            mockMvc.perform(
                    get(api.Events))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.events").isEmpty());
        }

        @Test
        @DisplayName("With 1 event - Returns event details")
        @AllEnabled(FeatureToggle.class)
        void findAll_EventDetails() throws Exception {
            eventJpaRepository.save(JpaEntity.Event.builder()
                    .title("march")
                    .description("a month")
                    .location("room1")
                    .startDateTime(LocalDateTime.of(2000, 9, 8, 7, 6, 5))
                    .endDateTime(LocalDateTime.of(2000, 9, 8, 7, 16, 5))
                    .authorName("Bob")
                    .authorJobTitle("tester")
                    .authorCompanyName("QA")
                    .build());

            mockMvc.perform(
                    get(api.Events))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.events.[0].id", greaterThan(0)))

                    .andExpect(jsonPath("$.events.[0].title").value("march"))
                    .andExpect(jsonPath("$.events.[0].description").value("a month"))
                    .andExpect(jsonPath("$.events.[0].location").value("room1"))

                    .andExpect(jsonPath("$.events.[0].period.startDateTime").value("2000-09-08T07:06:05"))
                    .andExpect(jsonPath("$.events.[0].period.endDateTime").value("2000-09-08T07:16:05"))

                    .andExpect(jsonPath("$.events.[0].author.name").value("Bob"))
                    .andExpect(jsonPath("$.events.[0].author.jobTitle").value("tester"))
                    .andExpect(jsonPath("$.events.[0].author.companyName").value("QA"));
        }

        @Test
        @DisplayName("With multiple events - Returns events")
        @AllEnabled(FeatureToggle.class)
        void findAll() throws Exception {
            eventJpaRepository.saveAll(Set.of(
                    Populated.event().title("serverless").build(),
                    Populated.event().title("container").build(),
                    Populated.event().title("blockchain").build()));

            mockMvc.perform(
                    get(api.Events))

                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.events.[*]", hasSize(3)))
                    .andExpect(jsonPath(
                            "$.events.[*].title",
                            containsInAnyOrder("serverless", "container", "blockchain")));
        }

        @Test
        @DisplayName("With inactive feature - Returns inactive error")
        @AllDisabled(FeatureToggle.class)
        void findAll_Error() throws Exception {
            mockMvc.perform(
                    get(api.Events))

                    .andExpect(status().isInternalServerError())

                    .andExpect(jsonPath("$.events").isEmpty())
                    .andExpect(jsonPath("$.errors.[0].code").value("FEATURE_INACTIVE"))
                    .andExpect(jsonPath("$.errors.[0].message").value("Feature " + EVENTS + " is not active"));
        }
    }
}