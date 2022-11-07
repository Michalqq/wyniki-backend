package com.akbp.racescore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementOutDto {

    private Long statementId;

    private Long eventId;

    private String name;

    private String description;

    private boolean fileExist;

    private String fileName;

    private Instant date;

    private Long userMod;

}
