package com.raihan.frontend.entities;

public abstract class Weapon {
    protected String weapon_name;
    protected float damage;
    protected float range;
    protected float durability = 100f;

    public float getDurability() { return durability; }
    public float getDamage(){ return damage; }
    public float getRange(){ return range; }
    public abstract void reduceDurability();
}
