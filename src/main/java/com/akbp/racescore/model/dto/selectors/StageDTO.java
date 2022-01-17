package com.akbp.racescore.model.dto.selectors;

import com.akbp.racescore.model.entity.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StageDTO {
    private Long stageId;
    private Long eventId;
    private String name;
    private Long distance;
    private Instant startTime;
    private Integer startFrequency;

    public StageDTO(Stage x) {
        this.stageId = x.getStageId();
        this.eventId = x.getEventId();
        this.name = x.getName();
        this.distance = x.getDistance();
        this.startTime = x.getStartTime();
        this.startFrequency = x.getStartFrequency();
    }
}
