package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByEventId(Long eventId);
}
