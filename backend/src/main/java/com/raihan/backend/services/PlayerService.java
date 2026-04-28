package com.raihan.backend.services;

import com.raihan.backend.model.Player;
import com.raihan.backend.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player createPlayer(Player player) {
        if(playerRepository.existsByUsername(player.getUsername())){
            throw new RuntimeException("Username already exists: " + player.getUsername());
        }
        return playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(UUID playerId){
        return playerRepository.findById(playerId);
    }

    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public Player updatePlayer(UUID playerId, Player player){
        Player existingPayer = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player with " + playerId + " does not exist"));
        if (player.getUsername() != null && playerRepository.existsByUsername(player.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        existingPayer.setUsername(player.getUsername());
        return playerRepository.save(player);
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
