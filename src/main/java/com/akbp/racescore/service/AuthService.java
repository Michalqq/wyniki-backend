package com.akbp.racescore.service;

import com.akbp.racescore.email.EmailSenderImpl;
import com.akbp.racescore.model.dto.auth.AuthRequest;
import com.akbp.racescore.model.dto.auth.JwtResponse;
import com.akbp.racescore.security.model.entity.User;
import com.akbp.racescore.security.model.jwt.JwtUtils;
import com.akbp.racescore.security.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailSenderImpl emailSender;

    public ResponseEntity<?> authenticateUser(AuthRequest request) {
        String username = request.getUsername();
        User userByEmail = userRepository.findByEmail(username);
        if (username.contains("@") && userByEmail != null)
            username = userByEmail.getUsername();

        if (!userRepository.existsByUsername(username))
            return ResponseEntity.badRequest().body("Brak użytkownika o podanym loginie");

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Błędne dane logowania");
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtils.generateJwtToken(auth);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, null, userDetails.getUsername(), null, roles));
    }

    public ResponseEntity<?> registerUser(AuthRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()) || userRepository.existsByEmail(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body("Error: Wybrany login jest zajęty");

        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body("Error: Wybrany email jest zajęty");

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        userRepository.save(user);

//        Set<Role> roles = new HashSet<>();
//        Role role = roleRepository.findByRole(USER_ROLE);
//        roles.add(role);
//        user.setRoles(roles);

        return ResponseEntity.ok(new JwtResponse(null, null, user.getUsername(), null, Collections.emptyList()));
    }

    public ResponseEntity<?> updatePassword(AuthRequest signupRequest) {
        String token = signupRequest.getToken().substring(1);
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findByUsername(userName);
        if (user == null)
            return ResponseEntity.badRequest().body("Nie znaleziono użytkownika na podstawie tokenu.");

        user.setPassword(encoder.encode(signupRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new JwtResponse(null, null, user.getUsername(), null, Collections.emptyList()));
    }

    public ResponseEntity<?> remindPassword(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null)
            return ResponseEntity.badRequest().body("Email nie istnieje w bazie danych.");

        emailSender.sendPasswordReminderEmail(user);

        return ResponseEntity.ok().body("Na podany email został wysłany link do resetowania hasła.");
    }
}
