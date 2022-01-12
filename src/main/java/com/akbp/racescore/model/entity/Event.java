package com.akbp.racescore.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE", nullable = false)
    private Instant date;

    @Column(name = "admin", nullable = false)
    private Long admin;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    @JsonIgnoreProperties("eventId")
    List<EventTeam> eventTeams;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    @JsonIgnoreProperties("eventId")
    List<Stage> stages;
}
