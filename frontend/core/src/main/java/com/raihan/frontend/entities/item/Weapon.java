package com.raihan.frontend.entities.item;

import com.raihan.frontend.entities.enemies.Enemies;

public interface Weapon {
    public float getRange();
    public float getDurability();
    public void Use(Enemies enemies);
}
