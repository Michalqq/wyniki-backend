package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.auth.AuthRequest;
import com.akbp.racescore.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin("*")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) {
        try {
            return authService.authenticateUser(request);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest signupRequest) {
        try {
            return authService.registerUser(signupRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/isMainAdmin")
    public boolean isMainAdmin(@RequestParam String login) {
        return login.equals("test") || login.equals("aaaa");
    }

    @PostMapping("/remindPassword")
    public ResponseEntity<?> remindPassword(@RequestParam String email) {
        try {
            return authService.remindPassword(email);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
