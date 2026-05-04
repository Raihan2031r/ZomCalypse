package com.raihan.frontend.pools;

public class BulletPool extends ObjectPool<Bullets>{
    @Override
    protected Bullets createObject() {
        return new Bullets();
    }

    @Override
    protected void resetObject(Bullets object) {

    }
}
