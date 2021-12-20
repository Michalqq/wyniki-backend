package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.CarClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarClassRepository extends JpaRepository<CarClass, Long> {
}
