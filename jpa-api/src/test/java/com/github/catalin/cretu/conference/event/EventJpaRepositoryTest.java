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
                        .name("Scott")
                        .jobTitle("manager")
                        .companyName("DDD")
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

        soft.assertThat(savedEvent.getName())
                .isEqualTo("Scott");
        soft.assertThat(savedEvent.getJobTitle())
                .isEqualTo("manager");
        soft.assertThat(savedEvent.getCompanyName())
                .isEqualTo("DDD");

        soft.assertAll();
    }

    @Test
    @DisplayName("update - Updates Event")
    void update() {
        var savedEvent = eventJpaRepository.save(Populated.event("DDD").build());
        flush();

        savedEvent.setName("EEE");
        var updatedEvent = eventJpaRepository.save(savedEvent);
        flush();

        soft.assertThat(
                eventJpaRepository.findById(updatedEvent.getId())
        )
                .get()
                .extracting(Event::getName)
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

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}