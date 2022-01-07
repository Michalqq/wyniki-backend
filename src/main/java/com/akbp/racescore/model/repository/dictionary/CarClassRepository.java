package com.akbp.racescore.model.repository.dictionary;

import com.akbp.racescore.model.entity.dictionary.CarClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarClassRepository extends JpaRepository<CarClass, Long> {
}
