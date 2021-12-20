package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
    Stage findByStageId(Long stageId);
}
