package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}
