package com.akbp.racescore.model.dto;

import com.akbp.racescore.model.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String name;
    private String description;
    private Instant date;
    private Instant signDeadline;
    private Long admin;

    public EventDTO(Event x) {
        this.eventId = x.getEventId();
        this.name = x.getName();
        this.description = x.getDescription();
        this.date = x.getDate();
        this.admin = x.getAdmin();
        this.signDeadline = x.getSignDeadline();
    }
}
