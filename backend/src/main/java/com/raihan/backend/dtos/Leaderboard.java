package com.raihan.backend.dtos;

public class Leaderboard {
    private PlayerInfo player;
    private int value;

    public Leaderboard(String username, int value) {
        this.player = new PlayerInfo(username);
        this.value = value;
    }

    public PlayerInfo getPlayer() { return player; }
    public int getValue() { return value; }

    public static class PlayerInfo {
        private String username;
        public PlayerInfo(String username) { this.username = username; }
        public String getUsername() { return username; }
    }
}