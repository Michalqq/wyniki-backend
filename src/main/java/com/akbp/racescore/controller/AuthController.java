package com.akbp.racescore.controller;

import com.akbp.racescore.model.dto.auth.AuthRequest;
import com.akbp.racescore.model.dto.auth.JwtResponse;
import com.akbp.racescore.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) {
        JwtResponse response = authService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest signupRequest) {
        return authService.registerUser(signupRequest);
    }
}
