package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.entity.dictionary.CarClass;
import com.akbp.racescore.model.enums.DriveType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID", updatable = false, nullable = false)
    private Long teamId;

    @Column(name = "DRIVER", nullable = false)
    private String driver;

    @Column(name = "CO_DRIVER")
    private String coDriver;

    @Column(name = "TEAM_NAME")
    private String teamName;

    @Column(name = "CAR", nullable = false)
    private String car;

    @Column(name = "LPG")
    private Boolean lpg;

    @ManyToOne()
    @JoinColumn(name = "CLASS")
    private CarClass carClass;

    @Column(name = "DRIVE_TYPE", nullable = false)
    private DriveType driveType;

    private Long userId;

}
