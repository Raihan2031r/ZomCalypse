package com.raihan.frontend.factories;

import com.raihan.frontend.pools.Bullets;
import com.raihan.frontend.pools.BulletPool;

import java.util.List;

public class BulletFactory {
    public final BulletPool bulletPool;

    public BulletFactory() {
        this.bulletPool = new BulletPool();
    }

    public Bullets spawnBullet(float startX, float startY, float dirX, float dirY, float speed, float damage, float range) {
        Bullets bullet = bulletPool.obtain();
        bullet.spawn(startX, startY, dirX, dirY, speed, damage, range);
        return bullet;
    }

    public void release(Bullets bullet) {
        bulletPool.release(bullet);
    }

    public void releaseAll() {
        bulletPool.releaseAll();
    }

    public List<Bullets> getInUse() {
        return bulletPool.getInUse();
    }
}
