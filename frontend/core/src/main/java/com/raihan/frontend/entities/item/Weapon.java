package com.raihan.frontend.entities.item;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.factories.BulletFactory;

public interface Weapon {
    public float getRange();
    public float getDamage();
    public float getDurability();
    public void Attack();
    public void Attack(BulletFactory bulletFactory);

    boolean isReloading();
    float getAttackDelay();

    String getName();
}
