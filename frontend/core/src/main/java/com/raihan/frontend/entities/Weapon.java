package com.raihan.frontend.entities;

public abstract class Weapon {
    private String weapon_name;
    private float damage;
    private float range;

    public float getDamage(){ return damage; }
    public float getRange(){ return range; }
}
