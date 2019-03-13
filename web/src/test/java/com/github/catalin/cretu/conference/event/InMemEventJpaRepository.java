package com.github.catalin.cretu.conference.event;

import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class InMemEventJpaRepository implements EventJpaRepository {

    private final Set<JpaEntity.Event> events;
    @Getter
    private Long currentId;

    InMemEventJpaRepository(final Set<JpaEntity.Event> events, final Long id) {
        this.events = events;
        this.currentId = id;
    }

    @Override
    public <S extends JpaEntity.Event> S save(final S entity) {
        events.add(entity);
        entity.setId(++currentId);
        return entity;
    }

    @Override
    public <S extends JpaEntity.Event> Iterable<S> saveAll(final Iterable<S> entities) {
        entities.forEach(events::add);
        return entities;
    }

    @Override
    public Optional<JpaEntity.Event> findById(final Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst();
    }

    @Override
    public Iterable<JpaEntity.Event> findAll() {
        return events;
    }

    @Override
    public void deleteAll() {
        events.clear();
    }

    @Override
    public boolean existsById(final Long aLong) {
        throw new NotImplementedException("");
    }

    @Override
    public Iterable<JpaEntity.Event> findAllById(final Iterable<Long> longs) {
        throw new NotImplementedException(EMPTY);
    }

    @Override
    public long count() {
        throw new NotImplementedException(EMPTY);
    }

    @Override
    public void deleteById(final Long aLong) {
        throw new NotImplementedException(EMPTY);
    }

    @Override
    public void delete(final JpaEntity.Event entity) {
        throw new NotImplementedException(EMPTY);
    }

    @Override
    public void deleteAll(final Iterable<? extends JpaEntity.Event> entities) {
        throw new NotImplementedException(EMPTY);
    }

    @Override
    public boolean exists(
            final LocalDateTime startDateTime,
            final LocalDateTime endDateTime,
            final String authorName,
            final String authorJobTitle,
            final String authorCompanyName) {
        return false;
    }
}