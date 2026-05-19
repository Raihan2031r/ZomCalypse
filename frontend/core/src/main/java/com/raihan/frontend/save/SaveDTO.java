package com.raihan.frontend.save;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

public class SaveDTO {
    public String gameDifficulty;
    public int zombiesKilled;
    public int gameDay;
    public int spentScore;

    public float playerHP;
    public float playerEnergy;
    public float playerSatiation;
    public float playerHydration;
    public Vec2Data playerPosition;
    public String currentWeapon;

    public List<ItemData> inventoryItems = new ArrayList<>();

    public int gameHour;
    public int gameMinute;

    public List<ZombieData> activeZombies = new ArrayList<>();

    public static class ZombieData {
        public Vec2Data pos;
        public float hp;
    }

    public static class ItemData {
        public String name;
        public float durability;
        public float impact;
    }

    public static class Vec2Data {
        public float x, y;
    }
}
