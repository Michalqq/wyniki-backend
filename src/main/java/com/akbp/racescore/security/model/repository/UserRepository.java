package com.akbp.racescore.security.model.repository;

import com.akbp.racescore.security.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String userName);

    boolean existsByUsername(String username);

    boolean existsByEmailIgnoreCase(String username);
}
