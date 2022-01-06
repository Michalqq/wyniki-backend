package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class EventTeam {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EVENT_ID", nullable = false)
    Long eventId;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_ID",
            nullable = false)
    Team team;

    @Column(name = "NUMBER", nullable = false)
    Integer number;

}
