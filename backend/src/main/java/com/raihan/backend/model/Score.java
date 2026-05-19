package com.raihan.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "score_id")
    private UUID scoreId;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "total_score", nullable = false)
    private int totalScore;

    @Column(name = "zombies_killed", nullable = false)
    private int zombiesKilled;

    @Column(name = "days_survived", nullable = false)
    private int daysSurvived;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UUID getScoreId() { return scoreId; }
    public void setScoreId(UUID scoreId) { this.scoreId = scoreId; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public int getZombiesKilled() { return zombiesKilled; }
    public void setZombiesKilled(int zombiesKilled) { this.zombiesKilled = zombiesKilled; }

    public int getDaysSurvived() { return daysSurvived; }
    public void setDaysSurvived(int daysSurvived) { this.daysSurvived = daysSurvived; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}