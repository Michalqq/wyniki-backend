package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID", updatable = false, nullable = false)
    private Long eventId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE", nullable = false)
    private Instant date;

    @Column(name = "USER_LOGIN", nullable = false)
    private String userLogin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventId", fetch = FetchType.EAGER)
    Set<EventTeam> eventTeams;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventId", fetch = FetchType.EAGER)
    Set<Stage> stages;
}
