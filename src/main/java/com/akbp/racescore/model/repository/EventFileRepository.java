package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.EventFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventFileRepository extends JpaRepository<EventFile, Long> {
}
