package com.raihan.frontend.states.enemyStates;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

public interface EnemyState {
    void enter(Enemies enemy);
    void update(Enemies enemy, Player player, float delta);
    void exit(Enemies enemy);
}
