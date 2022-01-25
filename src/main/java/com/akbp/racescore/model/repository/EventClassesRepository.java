package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.EventClasses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventClassesRepository extends JpaRepository<EventClasses, Long> {
    void deleteByEventId(Long eventId);
}