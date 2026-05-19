package com.raihan.frontend.factories;

import com.badlogic.gdx.math.Rectangle;
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

    public void spawnWave(DifficultyStrategy strategy, List<Rectangle> spawnAreas) {
        int zombieCount = (int) strategy.getZombiesPerWave();
        float hpMultiplier = strategy.getMode().equalsIgnoreCase("Hard") ? 1.5f : 1.0f;
        float speedMultiplier = strategy.getMode().equalsIgnoreCase("Hard") ? 1.3f : 1.0f;

        float ZOMBIE_WIDTH = 32f;
        float ZOMBIE_HEIGHT = 32f;

        for (int i = 0; i < zombieCount; i++) {
            Rectangle chosenArea = spawnAreas.get(random.nextInt(spawnAreas.size()));

            float maxSpawnWidth = Math.max(0, chosenArea.width - ZOMBIE_WIDTH);
            float maxSpawnHeight = Math.max(0, chosenArea.height - ZOMBIE_HEIGHT);

            float randomX = chosenArea.x + (random.nextFloat() * maxSpawnWidth);
            float randomY = chosenArea.y + (random.nextFloat() * maxSpawnHeight);

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

