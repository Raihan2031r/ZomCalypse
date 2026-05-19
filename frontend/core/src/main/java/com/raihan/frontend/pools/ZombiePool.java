package com.raihan.frontend.pools;

import com.raihan.frontend.entities.enemies.Zombies;

public class ZombiePool extends ObjectPool<Zombies> {
    private final float DETECT_RAD = 120f;
    private final float ATK_RAD = 7f;

    @Override
    protected Zombies createObject() {
        return new Zombies(0, 0, 100f, 15f, 60f, DETECT_RAD, ATK_RAD);
    }

    @Override
    protected void resetObject(Zombies object) {

    }
}
