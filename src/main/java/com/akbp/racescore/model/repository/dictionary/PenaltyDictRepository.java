package com.akbp.racescore.model.repository.dictionary;

import com.akbp.racescore.model.entity.dictionary.PenaltyDict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyDictRepository extends JpaRepository<PenaltyDict, Long> {

    List<PenaltyDict> findAllByOrderById();
}
