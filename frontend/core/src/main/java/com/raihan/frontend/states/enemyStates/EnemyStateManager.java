package com.raihan.frontend.states.enemyStates;

import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.enemies.Enemies;

import java.util.Stack;

public class EnemyStateManager {
    private final Stack<EnemyState> states;
    private final Enemies owner;

    public EnemyStateManager(Enemies owner) {
        this.owner = owner;
        this.states = new Stack<>();
    }

    public void set(EnemyState newState) {
        if (!states.isEmpty()) {
            states.pop().exit(owner);
        }
        states.push(newState);
        newState.enter(owner);
    }

    public void push(EnemyState newState) {
        states.push(newState);
        newState.enter(owner);
    }

    public void pop() {
        if (!states.isEmpty()) {
            states.pop().exit(owner);
        }
    }

    public void update(float delta, Player player) {
        if (!states.isEmpty()) {
            states.peek().update(owner, player, delta);
        }
    }

    public EnemyState getCurrentState() {
        if (!states.isEmpty()) {
            return states.peek();
        }
        return null;
    }
}
