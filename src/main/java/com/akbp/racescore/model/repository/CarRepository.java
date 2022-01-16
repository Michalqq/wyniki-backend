package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
