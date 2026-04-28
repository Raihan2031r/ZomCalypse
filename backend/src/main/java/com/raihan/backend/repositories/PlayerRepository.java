package com.raihan.backend.repositories;

import com.raihan.backend.model.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Optional<Player> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(
            value = "SELECT * FROM players ORDER BY CAST(game_state->>'days_survived' AS INTEGER) DESC NULLS LAST LIMIT :limit",
            nativeQuery = true
    )
    List<Player> findTopPlayers(@Param("limit") int limit);

    @Query(value = "select * from players", nativeQuery = true)
    List<Player> getAllPlayers();
}
