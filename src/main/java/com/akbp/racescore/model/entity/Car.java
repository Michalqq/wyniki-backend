package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.enums.DriveType;
import com.akbp.racescore.model.enums.EnginePetrol;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CAR_ID")
    private Long carId;

    @Column(name = "TEAM_ID", nullable = false)
    private Long teamId;

    private String brand;
    private String model;
    private String year;
    @Column(name = "LICENSE_PLATE")
    private String licensePlate;
    private String vin;
    @Column(name = "ENGINE_CAPACITY")
    private Double engineCapacity;
    private Boolean turbo;

    @Column(name = "DRIVE_TYPE")
    private String driveType;
    @Column(name = "PETROL")
    private String petrol;

    @Column(name = "DRIVE_TYPE", insertable = false, updatable = false)
    private DriveType driveTypeEnum;
    @Column(name = "PETROL", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private EnginePetrol petrolEnum;

    private String insurance;
    @Column(name = "INSURANCE_EXPIRY_DATE")
    private Instant insuranceExpiryDate;
    @Column(name = "CAR_INSPECTION_EXPIRY_DATE")
    private Instant carInspectionExpiryDate;
}
