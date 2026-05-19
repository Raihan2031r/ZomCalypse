package com.raihan.backend.services;

import com.raihan.backend.model.Player;
import com.raihan.backend.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Optional<Player> getPlayerById(UUID playerId){
        return playerRepository.findById(playerId);
    }

    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public Player updatePlayer(UUID playerId, Player playerInput){
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player with " + playerId + " does not exist"));

        if (playerInput.getUsername() != null && !playerInput.getUsername().equals(existingPlayer.getUsername())) {
            if (playerRepository.existsByUsername(playerInput.getUsername())){
                throw new RuntimeException("Username already exists");
            }
            existingPlayer.setUsername(playerInput.getUsername());
        }

        return playerRepository.save(existingPlayer);
    }

    public void deletePlayer(UUID playerId){
        if(!playerRepository.existsById(playerId)){
            throw new RuntimeException("Player with " + playerId + " does not exist");
        }
        playerRepository.deleteById(playerId);
    }

    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }
}