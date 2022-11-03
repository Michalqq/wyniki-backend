package com.akbp.racescore.model.entity.dictionary;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class CarClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_CLASS_ID", updatable = false, nullable = false)
    private Long carClassId;

    @Column(name = "NAME", nullable = false)
    private String name;

}
