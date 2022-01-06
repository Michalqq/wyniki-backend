package com.akbp.racescore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PenaltyByTeamDTO {
    private String driver;
    private String coDriver;
    private Integer number;
    private List<PenaltyDTO> penalties;
}
