package com.raihan.frontend.entities.item;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.factories.BulletFactory;
import com.raihan.frontend.pools.Bullets;

public class Rifle extends Items implements Weapon{
    private final float range = 112f;
    private int magazine = 30;

    protected Rifle() {
        super("Riffle");
        super.impact = 10f;
    }

    @Override
    public void update(float delta) {

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

            bulletFactory.spawnBullet(
                owner.getPosition().x,
                owner.getPosition().y,
                owner.getDirection().x,
                owner.getDirection().y,
                710f,
                impact,
                range
            );
        }
    }

    public void Reload(){
        this.magazine = 30;
    }
}
