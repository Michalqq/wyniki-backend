package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.dto.selectors.ClassesOption;
import com.akbp.racescore.model.dto.selectors.PsOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StgesAndClassesDTO {
    private List<PsOption> psOptions;
    private List<ClassesOption> classesOptions;
}
