package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.enums.DriveType;
import com.akbp.racescore.model.enums.EnginePetrol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {

    private String brand;
    private String model;
    private String year;
    private Double engineCapacity;
    private Boolean turbo;

    private String driveType;
    private String petrol;

    private DriveType driveTypeEnum;
    private EnginePetrol petrolEnum;
}
