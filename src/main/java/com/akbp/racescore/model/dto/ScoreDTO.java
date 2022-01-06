package com.akbp.racescore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    private Long stageScoreId;
    private Long teamId;
    private Long stageId;
    private Long stageStartTime;
    private Long score;
}
