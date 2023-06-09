package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STAGE_ID", updatable = false, nullable = false)
    private Long stageId;

    @Column(name = "EVENT_ID", nullable = false, insertable = false, updatable = false)
    private Long eventId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DISTANCE")
    private Long distance;

    @Column(name = "START_TIME")
    private Instant startTime;

    @Column(name = "START_FREQUENCY")
    private Integer startFrequency;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stageId", fetch = FetchType.EAGER)
    Set<StageScore> stageScores;
}
