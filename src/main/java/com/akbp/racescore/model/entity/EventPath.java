package com.akbp.racescore.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EventPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EVENT_ID", nullable = false, insertable = false, updatable = false)
    private Long eventId;

    @Column(name = "ENTRY_FEE_FILE")
    @Basic(fetch = FetchType.LAZY)
    private byte[] entryFeeFile;

    @Column(name = "DESCRIPTION")
    private String description;
}
