package com.raihan.backend.controller;

import com.raihan.backend.dtos.SaveGame;
import com.raihan.backend.model.Game;
import com.raihan.backend.model.Player;
import com.raihan.backend.repositories.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveGameToCloud(@RequestBody SaveGame request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player currentUser = (Player) authentication.getPrincipal();

        Game newSave = new Game();
        newSave.setPlayer(currentUser);

        newSave.setGameDifficulty(request.getGameDifficulty());
        newSave.setZombiesKilled(request.getZombiesKilled());
        newSave.setGameDay(request.getGameDay());
        newSave.setGameHour(request.getGameHour());
        newSave.setGameMinute(request.getGameMinute());
        newSave.setSpentScore(request.getSpentScore());
        newSave.setPlayerHp(request.getPlayerHP());
        newSave.setPlayerEnergy(request.getPlayerEnergy());
        newSave.setPlayerSatiation(request.getPlayerSatiation());
        newSave.setPlayerHydration(request.getPlayerHydration());
        newSave.setCurrentWeapon(request.getCurrentWeapon());

        newSave.setPlayerPosition(request.getPlayerPosition());
        newSave.setPlayerPosition(request.getPlayerPosition());

        newSave.setInventoryItems(request.getInventoryItems() != null
                ? request.getInventoryItems()
                : new ArrayList<>());

        newSave.setActiveZombies(request.getActiveZombies() != null
                ? request.getActiveZombies()
                : new ArrayList<>());

        gameRepository.save(newSave);
        return ResponseEntity.ok(Map.of("message", "Game state saved successfully to cloud!"));
    }

    @GetMapping
    public ResponseEntity<List<SaveGame>> getCloudSaves() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player currentUser = (Player) authentication.getPrincipal();

        List<Game> games = gameRepository.findByPlayerOrderByUpdatedAtDesc(currentUser);
        List<SaveGame> response = games.stream().map(game -> {
            SaveGame dto = new SaveGame();
            dto.setGameDifficulty(game.getGameDifficulty());
            dto.setZombiesKilled(game.getZombiesKilled());
            dto.setGameDay(game.getGameDay());
            dto.setGameHour(game.getGameHour());
            dto.setGameMinute(game.getGameMinute());
            dto.setSpentScore(game.getSpentScore());
            dto.setPlayerHP(game.getPlayerHp());
            dto.setPlayerEnergy(game.getPlayerEnergy());
            dto.setPlayerSatiation(game.getPlayerSatiation());
            dto.setPlayerHydration(game.getPlayerHydration());
            dto.setCurrentWeapon(game.getCurrentWeapon());

            dto.setPlayerPosition(game.getPlayerPosition());
            dto.setInventoryItems(game.getInventoryItems());
            dto.setActiveZombies(game.getActiveZombies());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}