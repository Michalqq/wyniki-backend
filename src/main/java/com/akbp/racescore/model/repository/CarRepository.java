package com.akbp.racescore.model.repository;

import com.akbp.racescore.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByInsuranceExpiryDateBetween(Instant expiryDateStart, Instant expiryDateEnd);
    List<Car> findByCarInspectionExpiryDateeBetween(Instant expiryDateStart, Instant expiryDateEnd);
}
