package com.raihan.frontend.factories;

import com.raihan.frontend.entities.enemies.Zombies;
import com.raihan.frontend.pools.ZombiePool;
import com.raihan.frontend.strategies.DifficultyStrategy;

import java.util.List;
import java.util.Random;

public class ZombieFactory {
    private final ZombiePool zombiePool = new ZombiePool();

    private static final float BASE_HP = 100f;
    private static final float BASE_ATK = 15f;
    private static final float BASE_SPEED = 60f;

    private final Random random;

    public ZombieFactory() {
        this.random = new Random();
    }

    public void spawnWave(DifficultyStrategy strategy, float spawnAreaX, float spawnAreaY, float areaWidth, float areaHeight) {
        int zombieCount = (int) strategy.getZombiesPerWave();
        float hpMultiplier = strategy.getMode().equalsIgnoreCase("Hard") ? 1.5f : 1.0f;
        float speedMultiplier = strategy.getMode().equalsIgnoreCase("Hard") ? 1.3f : 1.0f;

        for (int i = 0; i < zombieCount; i++) {
            float randomX = spawnAreaX + random.nextFloat() * areaWidth;
            float randomY = spawnAreaY + random.nextFloat() * areaHeight;

            Zombies zombie = zombiePool.obtain();

            zombie.spawn(
                randomX,
                randomY,
                BASE_HP * hpMultiplier,
                BASE_ATK,
                BASE_SPEED * speedMultiplier
            );
        }
    }

    public void release(Zombies zombie) {
        zombiePool.release(zombie);
    }

    public void releaseAll() {
        zombiePool.releaseAll();
    }

    public List<Zombies> getInUse() {
        return zombiePool.getInUse();
    }
}

