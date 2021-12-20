package com.akbp.racescore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Options {
    protected String label;
    protected String value;
    protected boolean defValue;
}
