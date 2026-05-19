package com.raihan.frontend.entities.item;

import com.raihan.frontend.factories.BulletFactory;

public class Rifle extends Items implements Weapon{
    private final float range = 112f;
    private int magazine = 30;
    private float reloadTime;

    public Rifle() {
        super("Rifle");
        super.impact = 10f;
    }

    @Override
    public void update(float delta) {
        if(reloadTime > 0) reloadTime -= delta;
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
        // Need bullets so this weapon can work
    }

    @Override
    public void Attack(BulletFactory bulletFactory) {
        if(magazine > 0){
            this.magazine -= 1;
            this.durability -= 0.2f;

            float spawnX = owner.getCollider().x + owner.getCollider().width / 2f;
            float spawnY = owner.getCollider().y + owner.getCollider().height / 2f;

            bulletFactory.spawnBullet(
                spawnX,
                spawnY,
                owner.getDirection().x,
                owner.getDirection().y,
                710f,
                impact,
                range
            );
        } else Reload();
    }

    @Override
    public boolean isReloading() {
        return reloadTime > 0;
    }

    @Override
    public float getAttackDelay() {
        return 0.25f;
    }

    public int getMagazine() {
        return magazine;
    }

    public void Reload() {
        if (magazine == 30) return;
        if (reloadTime > 0) return;

        if (owner != null && owner.getInventoryObject().consumeAmmo()) {
            reloadTime = 1.5f;
            this.magazine = 30;
        } else {
            System.out.println("Out of Ammo! Cari peluru di kotak jarahan!");
        }
    }
}
