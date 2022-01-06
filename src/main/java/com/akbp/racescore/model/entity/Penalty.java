package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PENALTY_ID", updatable = false, nullable = false)
    private Long penaltyId;

    @Column(name = "STAGE_ID", updatable = false)
    private Long stageId;

    @Column(name = "TEAM_ID", updatable = false)
    private Long teamId;

    @Column(name = "PENALTY_SEC")
    private Long penaltySec;

    @Column(name = "PENALTY_KIND")
    private Long penaltyKind;

    private String description;
}
