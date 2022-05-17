package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.EventClasses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventClassesRepository extends JpaRepository<EventClasses, Long> {
    void deleteByEventId(Long eventId);

    List<EventClasses> findByEventId(Long eventId);
}