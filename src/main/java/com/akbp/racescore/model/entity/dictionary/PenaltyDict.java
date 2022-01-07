package com.akbp.racescore.model.entity.dictionary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PenaltyDict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PENALTY_SEC", nullable = false)
    private String penaltySec;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "DISQUALIFICATION")
    private Boolean disqualification;
}
