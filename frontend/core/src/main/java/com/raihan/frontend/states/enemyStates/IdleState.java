package com.raihan.frontend.states.enemyStates;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public class IdleState implements EnemyState {

    @Override
    public void enter(Enemies enemy) {
        enemy.getVelocity().setZero();
    }

    @Override
    public void update(Enemies enemy, Player player, float delta) {
        if (enemy.getDetectionRadius().overlaps(player.getDetectionRadius())) {
            enemy.getEsm().set(new ChaseState());
        }
    }

    @Override
    public void exit(Enemies enemy) {
    }
}
