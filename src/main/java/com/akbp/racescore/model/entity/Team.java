package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.entity.dictionary.CarClass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Long teamId;

    @Column(name = "DRIVER", nullable = false)
    private String driver;
    @Column(name = "CO_DRIVER")
    private String coDriver;
    @Column(name = "TEAM_NAME")
    private String teamName;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    List<Car> cars;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CURRENT_CAR_ID")
    private Car currentCar;

    @Column(name = "CAR_CLASS")
    private Long carClassId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CAR_CLASS", insertable = false, updatable = false)
    private CarClass carClass;

    private Long userId;

    private String email;
    private String phone;
    @Column(name = "BIRTH_DATE")
    private Instant birthDate;
    @Column(name = "DRIVING_LICENSE")
    private String drivingLicense;
    @Column(name = "SPORT_LICENSE")
    private Boolean sportLicense;
    private String club;
    @Column(name = "EMERGENCY_PERSON")
    private String emergencyPerson;
    @Column(name = "EMERGENCY_PHONE")
    private String emergencyPhone;

    @Column(name = "CO_EMAIL")
    private String coEmail;
    @Column(name = "CO_PHONE")
    private String coPhone;
    @Column(name = "CO_BIRTH_DATE")
    private Instant coBirthDate;
    @Column(name = "CO_DRIVING_LICENSE")
    private String coDrivingLicense;
    @Column(name = "CO_SPORT_LICENSE")
    private Boolean coSportLicense;
    @Column(name = "CO_CLUB")
    private String coClub;

}
