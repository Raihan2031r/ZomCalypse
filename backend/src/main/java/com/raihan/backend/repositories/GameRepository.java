package com.raihan.backend.repositories;

import com.raihan.backend.model.Game;
import com.raihan.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByPlayerOrderByUpdatedAtDesc(Player player);
}