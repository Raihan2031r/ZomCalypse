package com.raihan.backend.dtos;

public class ScoreSubmit {
    private String playerId;
    private int value;
    private int zombies_killed;
    private int days_Passed;

    public String getPlayerId() { return playerId; }
    public int getValue() { return value; }
    public int getZombies_killed() { return zombies_killed; }
    public int getDays_Passed() { return days_Passed; }

    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public void setValue(int value) { this.value = value; }
    public void setZombies_killed(int zombies_killed) { this.zombies_killed = zombies_killed; }
    public void setDays_Passed(int days_Passed) { this.days_Passed = days_Passed; }
}