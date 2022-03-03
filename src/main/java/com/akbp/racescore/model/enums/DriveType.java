package com.akbp.racescore.model.enums;

import lombok.Getter;

@Getter
public enum DriveType {
    FWD(1L, "FWD"),
    RWD(2L, "RWD"),
    AWD(3L, "AWD");

    private final long id;
    private final String name;

    DriveType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DriveType getById(Long id) {
        if (id == null) return null;

        for (DriveType e : values()) {
            if (e.id == (id)) return e;
        }
        return null;
    }

}
