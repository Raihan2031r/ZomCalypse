package com.raihan.frontend.entities;

public class Spear extends Weapon {
    public Spear(){
        this.weapon_name = "Spear";
        this.damage = 20f;
        this.range = 30f;
    }

    @Override
    public void reduceDurability() {
        this.durability -= 2f;
    }
}
