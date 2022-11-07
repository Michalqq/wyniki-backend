package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.entity.dictionary.CarClass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class EventTeam {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", nullable = false, insertable = false, updatable = false)
    private Team team;

    @Column(name = "TEAM_ID", nullable = false)
    private Long teamId;

    @Column(name = "NUMBER", nullable = false)
    private Integer number;

    @Column(name = "START_ORDER")
    private Integer order;

    @Column(name = "FORCED_NUMBER")
    private Boolean forcedNumber;

    @Column(name = "ENTRY_FEE_PAID")
    private Boolean entryFeePaid;

    @Column(name = "TEAM_CHECKED")
    private Boolean teamChecked;

    @Column(name = "BK_POSITIVE")
    private Boolean bkPositive;

    @Column(name = "JOIN_DATE")
    private Instant joinDate;

    @Column(name = "DRIVER")
    private String driver;

    @Column(name = "CLUB")
    private String club;

    @Column(name = "CO_DRIVER")
    private String coDriver;

    @Column(name = "CO_CLUB")
    private String coClub;

    @Column(name = "TEAM_NAME")
    private String teamName;

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    private Car car;

    @Column(name = "ENTRY_FEE_FILE")
    @Basic(fetch = FetchType.LAZY)
    private byte[] entryFeeFile;

    @Column(name = "CAR_CLASS")
    private Long carClassId;

    @ManyToOne
    @JoinColumn(name = "CAR_CLASS", insertable = false, updatable = false)
    private CarClass carClass;
}
