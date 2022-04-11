package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.EventFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventFileRepository extends JpaRepository<EventFile, Long> {
    List<EventFile> findByEventId(Long eventId);

    EventFile findByIdAndEventId(Long fileId, Long eventId);
}
