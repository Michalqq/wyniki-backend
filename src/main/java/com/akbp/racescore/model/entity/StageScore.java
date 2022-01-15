package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
public class StageScore {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STAGE_ID")
    private Long stageId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(name = "TEAM_ID", updatable = false, insertable = false)
    private Long teamId;

    @Column(name = "TEAM_NUMBER", nullable = false)
    private Integer teamNumber;

    @Column(name = "SCORE")
    private Long score;

    @Column(name = "PENALTY")
    private Long penalty;

    @Column(name = "DISQUALIFIED")
    private Boolean disqualified;

    @Column(name = "USER_MOD")
    private Long userMod;

    @Column(name = "DATE_MOD")
    private Instant dateMod;
}
