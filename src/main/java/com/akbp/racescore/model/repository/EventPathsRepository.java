package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.EventPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPathsRepository extends JpaRepository<EventPath, Long> {
    void deleteByEventId(Long eventId);
}
