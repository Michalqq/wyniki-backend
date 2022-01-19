package com.akbp.racescore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
public class EventTeam {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_ID", nullable = false, insertable = false, updatable = false)
    private Team team;

    @Column(name = "TEAM_ID", nullable = false)
    private Long teamId;

    @Column(name = "NUMBER", nullable = false)
    private Integer number;

    @Column(name = "ENTRY_FEE_PAID")
    private Boolean entryFeePaid;

    @Column(name = "JOIN_DATE")
    private Instant joinDate;

    @Column(name = "ENTRY_FEE_FILE")
    @Basic(fetch = FetchType.LAZY)
    private byte[] entryFeeFile;
}
