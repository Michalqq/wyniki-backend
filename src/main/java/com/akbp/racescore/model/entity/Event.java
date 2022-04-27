package com.akbp.racescore.model.entity;

import com.akbp.racescore.security.model.entity.User;
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

    @Column(name = "LOGO_PATH")
    private String logoPath;

    @Column(name = "LOGO_PATH_FILE")
    private byte[] logoPathFile;

    @Column(name = "ORGANIZER")
    private String organizer;

    @Column(name = "DATE", nullable = false)
    private Instant date;

    @Column(name = "SIGN_DEADLINE")
    private Instant signDeadline;

    private Boolean started;

    @Column(name = "FWD_CLASSIFICATION")
    private Boolean fwdClassification;

    @Column(name = "RWD_CLASSIFICATION")
    private Boolean rwdClassification;

    @Column(name = "AWD_CLASSIFICATION")
    private Boolean awdClassification;

    @Column(name = "admin", nullable = false)
    private Long admin;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false, insertable = false, updatable = false)
    List<EventTeam> eventTeams;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    List<Stage> stages;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    List<EventClasses> eventClasses;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false)
    List<EventPath> eventPaths;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_ID", nullable = false, insertable = false, updatable = false)
    List<EventFile> eventFiles;

    @ManyToMany
    @JoinTable(name = "EVENT_REFEREE", joinColumns = @JoinColumn(name = "EVENT_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    List<User> referee;
}
