package com.raihan.backend.dtos;

public class SaveGame {
    private String gameDifficulty;
    private int zombiesKilled;
    private int gameDay;
    private int spentScore;
    private int gameHour;
    private int gameMinute;

    private float playerHP;
    private float playerEnergy;
    private float playerSatiation;
    private float playerHydration;
    private String currentWeapon;

    private Object playerPosition;
    private Object inventoryItems;
    private Object activeZombies;

    public String getGameDifficulty() { return gameDifficulty; }
    public void setGameDifficulty(String gameDifficulty) { this.gameDifficulty = gameDifficulty; }

    public int getZombiesKilled() { return zombiesKilled; }
    public void setZombiesKilled(int zombiesKilled) { this.zombiesKilled = zombiesKilled; }

    public int getGameDay() { return gameDay; }
    public void setGameDay(int gameDay) { this.gameDay = gameDay; }

    public int getSpentScore() { return spentScore; }
    public void setSpentScore(int spentScore) { this.spentScore = spentScore; }

    public int getGameHour() { return gameHour; }
    public void setGameHour(int gameHour) { this.gameHour = gameHour; }

    public int getGameMinute() { return gameMinute; }
    public void setGameMinute(int gameMinute) { this.gameMinute = gameMinute; }

    public float getPlayerHP() { return playerHP; }
    public void setPlayerHP(float playerHP) { this.playerHP = playerHP; }

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
}