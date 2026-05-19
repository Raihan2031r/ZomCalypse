package com.raihan.backend.controller;

import com.raihan.backend.model.Player;
import com.raihan.backend.services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/players")
@RestController
@CrossOrigin(origins = "*")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/me")
    public ResponseEntity<Player> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player currentUser = (Player) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping
    public ResponseEntity<List<Player>> allPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }
}