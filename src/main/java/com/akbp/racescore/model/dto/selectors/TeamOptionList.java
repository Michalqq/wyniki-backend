package com.akbp.racescore.model.dto.selectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TeamOptionList {
    List<Options> driveTypeOption;
    List<Options> petrolOption;

}
