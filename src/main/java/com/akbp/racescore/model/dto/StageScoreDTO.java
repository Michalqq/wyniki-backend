package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.EventTeam;
import com.akbp.racescore.model.entity.StageScore;
import com.akbp.racescore.model.enums.DriveType;
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
    private String club;
    private String coClub;
    private String car;
    private String brand;
    private String className;
    private String driveType;

    private String stageScore;
    private String timeTo;
    private String timeToFirst;

    private String scoreMin;
    private String scoreSec;
    private String scoreMiliSec;

    private String totalTime;
    private String totalPenalty;

    private Long totalTimeWithPenalty;

    public StageScoreDTO(StageScore score, EventTeam et) {
        this.stageScoreId = score.getId();
        this.driver = et.getDriver();
        this.coDriver = et.getCoDriver();
        this.teamName = et.getTeamName();
        this.club = et.getClub();
        this.brand = Optional.ofNullable(et.getCar()).map(x -> x.getBrand()).orElse("");
        this.coClub = et.getCoClub();

        this.car = Optional.ofNullable(et.getCar()).map(x -> x.getBrand() + " " + x.getModel()).orElse("");
        this.className = et.getCarClass().getName();
        this.number = et.getNumber();

        this.stageScore = ScoreToString.toString(score.getScore());
        this.timeTo = "-";
        this.timeToFirst = "-";
    }

    public StageScoreDTO(StageScoreSumDTO sssDTO) {
        this.driver = sssDTO.getDriver();
        this.coDriver = sssDTO.getCoDriver();
        this.coClub = sssDTO.getCoClub();
        this.club = sssDTO.getClub();
        this.teamName = sssDTO.getTeamName();
        this.car = sssDTO.getCar();
        this.brand = sssDTO.getBrand();
        this.className = sssDTO.getCarClass();
        this.number = sssDTO.getNumber();
        this.driveType = Optional.ofNullable(DriveType.getById(sssDTO.getDriveType())).map(x -> x.getName()).orElse("");
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
