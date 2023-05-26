package com.akbp.racescore.model.dto.event;

import com.akbp.racescore.model.entity.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicEventDto {
    private Long eventId;
    private String name;
    private String logoPath;
    private byte[] logoPathFile;

    public BasicEventDto(Event x) {
        this.eventId = x.getEventId();
        this.name = x.getName();
        this.logoPath = x.getLogoPath();
        this.logoPathFile = x.getLogoPathFile();
    }
}
