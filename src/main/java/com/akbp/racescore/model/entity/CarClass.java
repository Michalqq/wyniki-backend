package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class CarClass {
    @Id
    @Column(name = "CAR_CLASS_ID", updatable = false, nullable = false)
    private Long carClassId;

    @Column(name = "NAME", nullable = false)
    private String name;

}
