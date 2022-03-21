package com.akbp.racescore.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String username;
    private String password;
    private String token;
}
