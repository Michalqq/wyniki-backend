package com.akbp.racescore.model.dto.event;

import com.akbp.racescore.model.dto.RefereeDto;
import com.akbp.racescore.model.dto.selectors.StageDTO;
import com.akbp.racescore.model.entity.Event;
import com.akbp.racescore.model.entity.EventClasses;
import com.akbp.racescore.model.entity.EventFile;
import com.akbp.racescore.model.entity.EventPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SimpleEventDTO {
    private Long eventId;
    private String name;
    private String description;
    private String headDescription;
    private String footerDescription;
    private Instant date;
    private Instant signDeadline;
    private Long admin;
    private Boolean joined = false;
    private String organizer;
    private String logoPath;
    private Boolean started = false;
    private Boolean fwdClassification;
    private Boolean rwdClassification;
    private Boolean awdClassification;
    private Boolean carClassManual;
    private Boolean pzm;

    private List<RefereeDto> referee;
    private List<StageDTO> stages;
    private List<EventPath> eventPaths;

    public SimpleEventDTO(Event x) {
        this.eventId = x.getEventId();
        this.name = x.getName();
        this.description = x.getDescription();
        this.headDescription = x.getHeadDescription();
        this.footerDescription = x.getFooterDescription();
        this.date = x.getDate();
        this.admin = x.getAdmin();
        this.signDeadline = x.getSignDeadline();
        this.organizer = x.getOrganizer();
        this.logoPath = x.getLogoPath();
        this.started = x.getStarted();
        this.fwdClassification = x.getFwdClassification();
        this.rwdClassification = x.getRwdClassification();
        this.awdClassification = x.getAwdClassification();
        this.carClassManual = x.getCarClassManual();
        this.pzm = x.getPzm();

        this.eventPaths = x.getEventPaths();
    }
}
