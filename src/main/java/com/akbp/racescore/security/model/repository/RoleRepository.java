package com.akbp.racescore.security.model.repository;

import com.akbp.racescore.security.model.entity.ERole;
import com.akbp.racescore.security.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(ERole role);
}
