package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Stage {
    @Id
    @Column(name = "STAGE_ID", updatable = false, nullable = false)
    private Long stageId;

    @Column(name = "EVENT_ID", nullable = false)
    Long eventId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DISTANCE")
    private Long distance;

    @Column(name = "START_TIME")
    private Long startTime;

    @Column(name = "START_FREQUENCY")
    private Integer startFrequency;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stageId", fetch = FetchType.EAGER)
    Set<StageScore> stageScores;
}
