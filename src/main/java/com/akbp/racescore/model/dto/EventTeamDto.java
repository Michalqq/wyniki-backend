package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.Car;
import com.akbp.racescore.model.entity.Team;
import com.akbp.racescore.model.entity.dictionary.CarClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventTeamDto {

    private Long id;

    private Long eventId;

    private Team team;

    private Long teamId;

    private Integer number;

    private Integer order;

    private Boolean forcedNumber;

    private Boolean entryFeePaid;

    private Boolean teamChecked;

    private Boolean bkPositive;

    private Instant joinDate;

    private String driver;

    private String club;

    private String coDriver;

    private String coClub;

    private String teamName;

    private boolean entryFeeFileExist;

    private Car car;

    private Long carClassId;

    private CarClass carClass;
}
