package com.raihan.frontend.entities.item;

import com.raihan.frontend.factories.BulletFactory;

// Item with category weapon
public class Spear extends Items implements Weapon {
    private float range = 32f;

    public Spear(){
        super("Spear");
        this.impact = 30f;
    }

    @Override
    public void update(float delta) {
        // doesn't rotten or something else
    }

    @Override
    public float getRange() {
        return range;
    }

    @Override
    public float getDamage() {
        return impact;
    }

    @Override
    public void Attack() {
        this.durability -= 2f;
    }

    @Override
    public void Attack(BulletFactory bulletFactory) {
        // Doesn't need a bullet
    }

    @Override
    public boolean isReloading() {
        // Doesn't need reload
        return false;
    }

    @Override
    public float getAttackDelay() {
        return 0.5f;
    }
}
