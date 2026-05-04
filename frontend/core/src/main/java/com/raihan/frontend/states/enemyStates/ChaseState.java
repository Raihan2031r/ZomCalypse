package com.raihan.frontend.states.enemyStates;

import com.badlogic.gdx.math.Vector2;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public class ChaseState implements EnemyState{
    private Vector2 direction;

    @Override
    public void enter(Enemies enemy) {
        direction = new Vector2();
    }

    @Override
    public void update(Enemies enemy, Player player, float delta) {
        if (!enemy.getDetectionRadius().overlaps(player.getDetectionRadius())) {
            enemy.getEsm().set(new IdleState());
            return;
        }
        if (enemy.getAttackRadius().overlaps(player.getDetectionRadius())) {
            enemy.getEsm().push(new AttackState());
            return;
        }
        Vector2 playerPos = player.getPosition();
        Vector2 enemyPos = enemy.getPosition();
        direction.set(playerPos.x - enemyPos.x, playerPos.y - enemyPos.y).nor();
        enemy.getVelocity().set(direction.x * enemy.getSpeed(), direction.y * enemy.getSpeed());
    }

    @Override
    public void exit(Enemies enemy) {
        enemy.getVelocity().setZero();
    }
}
