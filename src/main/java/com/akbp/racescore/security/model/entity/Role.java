package com.akbp.racescore.security.model.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERole role;

    @Override
    public String getAuthority() {
        return role.name();
    }
}
