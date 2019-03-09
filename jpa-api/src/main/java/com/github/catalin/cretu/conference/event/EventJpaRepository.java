package com.github.catalin.cretu.conference.event;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EventJpaRepository extends CrudRepository<JpaEntity.Event, Long> {
    //no-op
}
