package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.utils.ScoreToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StageScoreDTO {

    private Long stageScoreId;
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

    private String scoreMin;
    private String scoreSec;
    private String scoreMiliSec;

    private String totalTime;
    private String totalPenalty;

    private Long totalTimeWithPenalty;

    public StageScoreDTO(StageScore score) {
        this.stageScoreId = score.getId();
        this.driver = score.getTeam().getDriver();
        this.coDriver = score.getTeam().getCoDriver();
        this.teamName = score.getTeam().getTeamName();
        this.car = Optional.ofNullable(score.getTeam().getCurrentCar()).map(x -> x.getBrand() + " " + x.getModel()).orElse("");
        this.className = score.getTeam().getCarClass().getName();
        this.number = score.getTeamNumber();

        this.stageScore = ScoreToString.toString(score.getScore());
        this.timeTo = "-";
        this.timeToFirst = "-";
    }

    public StageScoreDTO(StageScoreSumDTO sssDTO) {
        this.driver = sssDTO.getDriver();
        this.coDriver = sssDTO.getCoDriver();
        this.teamName = sssDTO.getTeamName();
        this.car = sssDTO.getCar();
        this.className = sssDTO.getCarClass();
        this.number = sssDTO.getNumber();
        this.totalPenalty = String.valueOf(sssDTO.getPenalty());
        this.totalTimeWithPenalty = sssDTO.getSumScore() + (sssDTO.getPenalty() * 1000);
        this.stageScore = ScoreToString.toString(sssDTO.getSumScore());
        this.totalTime = ScoreToString.toString(this.totalTimeWithPenalty);
    }

    public void setScoreFromTotalScore(StageScore score) {
        this.scoreMin = ScoreToString.getMinutes(score.getScore());
        this.scoreSec = ScoreToString.getSeconds(score.getScore());
        this.scoreMiliSec = ScoreToString.getMilis(score.getScore());
    }
}
