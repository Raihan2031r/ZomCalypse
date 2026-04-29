package com.raihan.frontend.entities.item;

import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.entities.Player;

// Item with category weapon
public class Spear extends Items implements Weapon {
    private float range = 15f;

    public Spear(){
        super("Spear");
        this.impact = 25f;
    }

    @Override
    public void Use(Player player) {
        // cannot friendly fire
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public void Use(Enemies enemies) {
        enemies.takeDamage(impact);
        this.durability -= 2f;
    }

    @Override
    public void reduceDurability() {
        // Not reduced on regular
    }
}
