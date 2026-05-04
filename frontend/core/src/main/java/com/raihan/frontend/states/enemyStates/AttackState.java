package com.raihan.frontend.states.enemyStates;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public class AttackState implements EnemyState{
    private float attackTimer;
    private final float PREPARE_ATTACK = 0.4f;
    private final float RECOVERY_DELAY = 0.6f;
    private boolean hasDealtDamage;

    @Override
    public void enter(Enemies enemy) {
        enemy.getVelocity().setZero();
        attackTimer = 0f;
        hasDealtDamage = false;
    }

    @Override
    public void update(Enemies enemy, Player player, float delta) {
        attackTimer += delta;
        if (attackTimer >= PREPARE_ATTACK && !hasDealtDamage) {
            if (enemy.getAttackRadius().overlaps(player.getDetectionRadius())) {
                player.takeImpact(-enemy.getAtk());
            }
            hasDealtDamage = true;
        }
        if (attackTimer >= (PREPARE_ATTACK + RECOVERY_DELAY)) {
            enemy.getEsm().pop();
        }
    }

    @Override
    public void exit(Enemies enemy) {
    }
}
