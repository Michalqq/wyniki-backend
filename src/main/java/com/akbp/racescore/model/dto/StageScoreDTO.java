package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.utils.ScoreToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StageScoreDTO {

    private int place;
    private Integer number;

    private String driver;
    private String coDriver;
    private String teamName;
    private String car;
    private String className;

    private String stageScore;
    private String timeTo;
    private String timeToFirst;

    private String totalTime;
    private String totalPenalty;

    private Long totalTimeWithPenalty;

    public StageScoreDTO(StageScore score) {
        this.driver = score.getTeam().getDriver();
        this.coDriver = score.getTeam().getCoDriver();
        this.teamName = "Todo";
        this.car = score.getTeam().getCar();
        this.className = score.getTeam().getCarClass().getName();
        this.number = score.getTeamNumber();

        this.stageScore = ScoreToString.toString(score.getScore());
        this.timeTo = "-";
        this.timeToFirst = "-";
    }

    public StageScoreDTO(StageScoreSumDTO sssDTO) {
        this.driver = sssDTO.getDriver();
        this.coDriver = sssDTO.getCoDriver();
        //this.teamName = sssDTO
        this.car = sssDTO.getCar();
        this.className = sssDTO.getCarClass();
        this.number = sssDTO.getNumber();
        this.stageScore = ScoreToString.toString(sssDTO.getSumScore());
        this.totalTimeWithPenalty = sssDTO.getSumScore() + sssDTO.getPenalty();
    }
}
