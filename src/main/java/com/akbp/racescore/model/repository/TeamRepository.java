package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
