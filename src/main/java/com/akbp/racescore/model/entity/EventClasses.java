package com.akbp.racescore.model.entity;

import com.akbp.racescore.model.entity.dictionary.CarClass;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EventClasses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EVENT_ID", nullable = false, insertable = false, updatable = false)
    private Long eventId;

    @Column(name = "CAR_CLASS_ID")
    private Long carClassId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CAR_CLASS_ID", insertable = false, updatable = false)
    private CarClass carClass;

    @Column(name = "MAX_ENGINE_CAPACITY", nullable = false)
    private Double maxEngineCapacity;

    @Column(name = "awd")
    private boolean awd;
}
