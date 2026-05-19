package com.raihan.frontend.pools;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BulletPool extends ObjectPool<Bullets>{
    @Override
    protected Bullets createObject() {
        return new Bullets();
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (Bullets bullet : getInUse()) {
            bullet.render(shapeRenderer);
        }
    }

    @Override
    protected void resetObject(Bullets object) {

    }
}
