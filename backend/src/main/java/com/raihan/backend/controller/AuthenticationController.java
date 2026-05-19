package com.raihan.backend.controller;

import com.raihan.backend.dtos.LoginUser;
import com.raihan.backend.dtos.RegisterUser;
import com.raihan.backend.model.Player;
import com.raihan.backend.services.AuthenticationService;
import com.raihan.backend.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUser registerRequest) {
        try {
            Player registeredUser = authenticationService.signup(registerRequest);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUser loginRequest) {
        try {
            Player authenticatedUser = authenticationService.authenticate(loginRequest);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            return ResponseEntity.ok(Map.of("token", jwtToken));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Username atau Password salah!"));
        }
    }
}