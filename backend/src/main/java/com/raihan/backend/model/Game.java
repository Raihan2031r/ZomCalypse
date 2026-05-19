package com.raihan.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "game_id")
    private UUID gameId;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "game_difficulty")
    private String gameDifficulty;

    @Column(name = "zombies_killed")
    private int zombiesKilled;

    @Column(name = "game_day")
    private int gameDay;

    @Column(name = "game_hour")
    private int gameHour;

    @Column(name = "game_minute")
    private int gameMinute;

    @Column(name = "spent_score")
    private int spentScore;

    @Column(name = "player_hp")
    private float playerHp;

    @Column(name = "player_energy")
    private float playerEnergy;

    @Column(name = "player_satiation")
    private float playerSatiation;

    @Column(name = "player_hydration")
    private float playerHydration;

    @Column(name = "current_weapon")
    private String currentWeapon;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "player_position", columnDefinition = "jsonb")
    private Object playerPosition;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "inventory_items", columnDefinition = "jsonb")
    private Object inventoryItems;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "active_zombies", columnDefinition = "jsonb")
    private Object activeZombies;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UUID getGameId() { return gameId; }
    public void setGameId(UUID gameId) { this.gameId = gameId; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public String getGameDifficulty() { return gameDifficulty; }
    public void setGameDifficulty(String gameDifficulty) { this.gameDifficulty = gameDifficulty; }
    public int getZombiesKilled() { return zombiesKilled; }
    public void setZombiesKilled(int zombiesKilled) { this.zombiesKilled = zombiesKilled; }
    public int getGameDay() { return gameDay; }
    public void setGameDay(int gameDay) { this.gameDay = gameDay; }
    public int getGameHour() { return gameHour; }
    public void setGameHour(int gameHour) { this.gameHour = gameHour; }
    public int getGameMinute() { return gameMinute; }
    public void setGameMinute(int gameMinute) { this.gameMinute = gameMinute; }
    public int getSpentScore() { return spentScore; }
    public void setSpentScore(int spentScore) { this.spentScore = spentScore; }
    public float getPlayerHp() { return playerHp; }
    public void setPlayerHp(float playerHp) { this.playerHp = playerHp; }
    public float getPlayerEnergy() { return playerEnergy; }
    public void setPlayerEnergy(float playerEnergy) { this.playerEnergy = playerEnergy; }
    public float getPlayerSatiation() { return playerSatiation; }
    public void setPlayerSatiation(float playerSatiation) { this.playerSatiation = playerSatiation; }
    public float getPlayerHydration() { return playerHydration; }
    public void setPlayerHydration(float playerHydration) { this.playerHydration = playerHydration; }
    public String getCurrentWeapon() { return currentWeapon; }
    public void setCurrentWeapon(String currentWeapon) { this.currentWeapon = currentWeapon; }
    public Object getPlayerPosition() { return playerPosition; }
    public void setPlayerPosition(Object playerPosition) { this.playerPosition = playerPosition; }
    public Object getInventoryItems() { return inventoryItems; }
    public void setInventoryItems(Object inventoryItems) { this.inventoryItems = inventoryItems; }
    public Object getActiveZombies() { return activeZombies; }
    public void setActiveZombies(Object activeZombies) { this.activeZombies = activeZombies; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}