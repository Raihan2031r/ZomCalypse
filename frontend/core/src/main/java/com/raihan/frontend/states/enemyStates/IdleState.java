package com.raihan.frontend.states.enemyStates;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

import java.util.Random;
public class IdleState implements EnemyState {

    private float idleTimer;
    private float idleDuration;
    Random random = new Random();

    @Override
    public void enter(Enemies enemy) {
        enemy.getVelocity().setZero();
        idleTimer = 0f;
        idleDuration = random.nextFloat(1.5f, 3.5f);
    }

    @Override
    public void update(Enemies enemy, Player player, float delta) {
        if (enemy.getDetectionRadius().overlaps(player.getDetectionRadius())) {
            enemy.getEsm().set(new ChaseState());
            return;
        }

        idleTimer += delta;
        if (idleTimer >= idleDuration) {
            enemy.getEsm().set(new PatrolState());
        }
    }

    @Override
    public void exit(Enemies enemy) {
    }
}
