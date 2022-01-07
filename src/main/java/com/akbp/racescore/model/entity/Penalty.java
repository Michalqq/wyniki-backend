package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.entity.dictionary.PenaltyDict;
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

    @Column(name = "TEAM_ID")
    private Long teamId;

    @Column(name = "PENALTY_SEC")
    private Long penaltySec;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PENALTY_KIND", insertable = false, updatable = false)
    private PenaltyDict penaltyDict;

    @Column(name = "PENALTY_KIND")
    private Long penaltyKind;

    private String description;
}
