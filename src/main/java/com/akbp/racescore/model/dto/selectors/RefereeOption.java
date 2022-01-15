package com.akbp.racescore.model.dto.selectors;

import com.akbp.racescore.security.model.entity.User;

public class RefereeOption extends Options {
    public RefereeOption(String label, String value, boolean defValue) {
        super(label, value, defValue);
    }

    public RefereeOption(User x) {
        super(x.getUsername(), String.valueOf(x.getUserId()), false);
    }
}
