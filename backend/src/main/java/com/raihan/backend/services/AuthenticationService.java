package com.raihan.backend.services;

import com.raihan.backend.dtos.LoginUser;
import com.raihan.backend.dtos.RegisterUser;
import com.raihan.backend.model.Player;
import com.raihan.backend.repositories.PlayerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PlayerRepository playerRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder){
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.playerRepository = playerRepository;
    }

    public Player signup(RegisterUser request) {
        if (playerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username sudah digunakan!");
        }
        Player user = new Player()
                .setUsername(request.getUsername())
                .setPassword(passwordEncoder.encode(request.getPassword()));

        return playerRepository.save(user);
    }

    public Player authenticate(LoginUser request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return playerRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Player tidak ditemukan"));
    }
}
