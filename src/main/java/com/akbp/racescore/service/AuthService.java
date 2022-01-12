package com.akbp.racescore.service;

import com.akbp.racescore.model.dto.auth.AuthRequest;
import com.akbp.racescore.model.dto.auth.JwtResponse;
import com.akbp.racescore.security.model.entity.Role;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.jwt.JwtUtils;
import com.akbp.racescore.security.model.repository.RoleRepository;
import com.akbp.racescore.security.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.akbp.racescore.security.model.entity.ERole.USER_ROLE;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse authenticateUser(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtils.generateJwtToken(auth);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList());

        return new JwtResponse(jwt, null, userDetails.getUsername(), null, roles);
    }

    public ResponseEntity<?> registerUser(AuthRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body("Error: Wybrany login jest zajęty");

        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body("Error: Wybrany email jest zajęty");

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role role = roleRepository.findByRole(USER_ROLE);
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(user.getUsername() + " został zarejestrowany");
    }
}
