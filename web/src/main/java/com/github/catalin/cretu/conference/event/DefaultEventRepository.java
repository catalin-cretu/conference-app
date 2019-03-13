package com.github.catalin.cretu.conference.event;

import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

public class DefaultEventRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;

    DefaultEventRepository(final EventJpaRepository eventJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
    }

    @Override
    public Set<Event> findAll() {
        return StreamSupport.stream(eventJpaRepository.findAll().spliterator(), true)
                .map(DefaultEventRepository::toEvent)
                .collect(toSet());
    }

    private static Event toEvent(final JpaEntity.Event eventEntity) {
        return Event.builder()
                .id(eventEntity.getId())

                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .location(eventEntity.getLocation())

                .period(Period.builder()
                        .startDateTime(eventEntity.getStartDateTime())
                        .endDateTime(eventEntity.getEndDateTime())
                        .build())

                .author(Author.builder()
                        .name(eventEntity.getAuthorName())
                        .jobTitle(eventEntity.getAuthorJobTitle())
                        .companyName(eventEntity.getAuthorCompanyName())
                        .build())
                .build();
    }

    @Override
    public Event save(final Event event) {
        JpaEntity.Event savedJpaEntityEvent = eventJpaRepository.save(toJpaEvent(event));

        return toEvent(savedJpaEntityEvent);
    }

    @Override
    public boolean exists(final Event event) {
        var period = event.getPeriod();
        var author = event.getAuthor();

        return eventJpaRepository.exists(
                period.getStartDateTime(),
                period.getEndDateTime(),
                author.getName(),
                author.getJobTitle(),
                author.getCompanyName());
    }

    private static JpaEntity.Event toJpaEvent(final Event event) {
        return JpaEntity.Event.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .startDateTime(event.getPeriod().getStartDateTime())
                .endDateTime(event.getPeriod().getEndDateTime())
                .authorName(event.getAuthor().getName())
                .authorJobTitle(event.getAuthor().getJobTitle())
                .authorCompanyName(event.getAuthor().getCompanyName())
                .build();
    }
}
