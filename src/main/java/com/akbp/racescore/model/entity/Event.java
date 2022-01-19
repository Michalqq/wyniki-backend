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

    @Column(name = "DATE", nullable = false)
    private Instant date;

    @Column(name = "SIGN_DEADLINE")
    private Instant signDeadline;

    private Boolean started;

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

    @ManyToMany
    @JoinTable(name = "EVENT_REFEREE", joinColumns = @JoinColumn(name = "EVENT_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    List<User> referee;
}
