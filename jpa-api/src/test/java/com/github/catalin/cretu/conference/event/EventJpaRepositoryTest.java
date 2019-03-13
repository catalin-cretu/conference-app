package com.github.catalin.cretu.conference.event;

import com.github.catalin.cretu.conference.event.JpaEntity.Event;
import com.github.catalin.cretu.conference.fixture.Populated;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@RepositoryTestExtension
class EventJpaRepositoryTest {

    private final SoftAssertions soft = new SoftAssertions();

    private final EventJpaRepository eventJpaRepository;
    private final EntityManager entityManager;

    @Autowired
    EventJpaRepositoryTest(final EventJpaRepository eventJpaRepository, final EntityManager entityManager) {
        this.eventJpaRepository = eventJpaRepository;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("save - Persists Event")
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void save() {
        var event =
                eventJpaRepository.save(Event.builder()
                        .title("Prezi")
                        .description("A Presentation")
                        .location("room-A")
                        .startDateTime(LocalDateTime.of(2000, 1, 2, 13, 0, 0))
                        .endDateTime(LocalDateTime.of(2000, 1, 2, 13, 45, 0))
                        .authorName("Scott")
                        .authorJobTitle("manager")
                        .authorCompanyName("DDD")
                        .build());
        flush();

        soft.assertThat(event.getId())
                .isNotNull();

        var optionalSavedEvent = eventJpaRepository.findById(event.getId());

        soft.assertThat(optionalSavedEvent)
                .isPresent();
        var savedEvent = optionalSavedEvent.get();

        soft.assertThat(savedEvent.getId())
                .isNotNull();

        soft.assertThat(savedEvent.getStartDateTime())
                .isEqualTo(LocalDateTime.of(2000, 1, 2, 13, 0, 0));
        soft.assertThat(savedEvent.getEndDateTime())
                .isEqualTo(LocalDateTime.of(2000, 1, 2, 13, 45, 0));

        soft.assertThat(savedEvent.getTitle())
                .isEqualTo("Prezi");
        soft.assertThat(savedEvent.getDescription())
                .isEqualTo("A Presentation");
        soft.assertThat(savedEvent.getLocation())
                .isEqualTo("room-A");

        soft.assertThat(savedEvent.getAuthorName())
                .isEqualTo("Scott");
        soft.assertThat(savedEvent.getAuthorJobTitle())
                .isEqualTo("manager");
        soft.assertThat(savedEvent.getAuthorCompanyName())
                .isEqualTo("DDD");

        soft.assertAll();
    }

    @Test
    @DisplayName("update - Updates Event")
    void update() {
        var savedEvent = eventJpaRepository.save(Populated.event("DDD").build());
        flush();

        savedEvent.setAuthorName("EEE");
        var updatedEvent = eventJpaRepository.save(savedEvent);
        flush();

        soft.assertThat(
                eventJpaRepository.findById(updatedEvent.getId())
        )
                .get()
                .extracting(Event::getAuthorName)
                .isEqualTo("EEE");

        soft.assertAll();
    }

    @Test
    @DisplayName("findAll - Returns All Events")
    void findAll() {
        eventJpaRepository.save(Populated.event().title("A").build());
        eventJpaRepository.save(Populated.event().title("B").build());
        eventJpaRepository.save(Populated.event().title("C").build());
        flush();

        soft.assertThat(eventJpaRepository.findAll())
                .hasSize(3)
                .extracting(Event::getTitle)
                .containsExactlyInAnyOrder("A", "B", "C");

        soft.assertAll();
    }

    @Test
    @DisplayName("delete - Deletes Event")
    void delete() {
        var savedEvent = eventJpaRepository.save(Populated.event().build());
        flush();

        eventJpaRepository.delete(savedEvent);
        flush();

        soft.assertThat(
                eventJpaRepository.findById(savedEvent.getId())
        ).isNotPresent();

        soft.assertAll();
    }

    @Test
    @DisplayName("exists - Event exists - Returns true")
    void exists() {
        soft.assertThat(
                eventJpaRepository.exists(
                        LocalDateTime.of(2003, 3, 3, 13, 0),
                        LocalDateTime.of(2003, 3, 3, 13, 30),
                        "Bobby",
                        "support",
                        "Google"))
                .isFalse();

        eventJpaRepository.save(Populated.event()
                .startDateTime(LocalDateTime.of(2003, 3, 3, 13, 0))
                .endDateTime(LocalDateTime.of(2003, 3, 3, 13, 30))
                .authorName("Bobby")
                .authorJobTitle("support")
                .authorCompanyName("Google")
                .build());
        flush();

        boolean eventExists = eventJpaRepository.exists(
                LocalDateTime.of(2003, 3, 3, 13, 0),
                LocalDateTime.of(2003, 3, 3, 13, 30),
                "Bobby",
                "support",
                "Google");

        soft.assertThat(eventExists).isTrue();
        soft.assertAll();
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}