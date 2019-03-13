package com.github.catalin.cretu.conference.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
interface EventJpaRepository extends CrudRepository<JpaEntity.Event, Long> {

    @Query("select " +
            "  case" +
            "    when count(*) > 0" +
            "    then true" +
            "    else false" +
            "  end " +
            "from JpaEntity$Event event " +
            "where " +
            "    event.startDateTime = :startDateTime " +
            "and event.endDateTime = :endDateTime " +
            "and event.authorName = :authorName " +
            "and event.authorJobTitle = :authorJobTitle " +
            "and event.authorCompanyName = :authorCompanyName")
    boolean exists(
            @Param("startDateTime") final LocalDateTime startDateTime,
            @Param("endDateTime") final LocalDateTime endDateTime,
            @Param("authorName") final String authorName,
            @Param("authorJobTitle") final String authorJobTitle,
            @Param("authorCompanyName") final String authorCompanyName);
}
