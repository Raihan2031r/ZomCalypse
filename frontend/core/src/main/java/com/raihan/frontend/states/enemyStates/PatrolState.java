package com.raihan.frontend.states.enemyStates;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.states.playerStates.DyingState;

import java.util.Random;

public class PatrolState implements EnemyState {

    private float patrolTimer;
    private float patrolDuration;
    private Vector2 patrolDirection;
    private Random random = new Random();

    @Override
    public void enter(Enemies enemy) {
        patrolTimer = 0f;
        patrolDuration = random.nextFloat(2f, 4f);

        patrolDirection = new Vector2();

        float angle = random.nextFloat(0f, 360f);

        patrolDirection.x = MathUtils.cosDeg(angle);
        patrolDirection.y = MathUtils.sinDeg(angle);
        patrolDirection.nor();

        float patrolSpeed = enemy.getSpeed() * 0.5f;
        enemy.getVelocity().set(patrolDirection.x * patrolSpeed, patrolDirection.y * patrolSpeed);
    }

    @Override
    public void update(Enemies enemy, Player player, float delta) {
        if (enemy.getDetectionRadius().overlaps(player.getDetectionRadius()) &&
            !(player.getState() instanceof DyingState))
        {
            enemy.getEsm().set(new ChaseState());
            return;
        }

        patrolTimer += delta;

        if (patrolTimer >= patrolDuration) {
            enemy.getEsm().set(new IdleState());
        }
    }

    @Override
    public void exit(Enemies enemy) {
        enemy.getVelocity().setZero();
    }
}
