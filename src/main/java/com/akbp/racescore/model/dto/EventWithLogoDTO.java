package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventWithLogoDTO extends EventDTO {
    private byte[] logoPathFile;

    public EventWithLogoDTO(Event x) {
        super(x);
        this.logoPathFile = x.getLogoPathFile();
    }
}
