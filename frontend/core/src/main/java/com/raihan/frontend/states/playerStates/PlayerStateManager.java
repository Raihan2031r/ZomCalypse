package com.raihan.frontend.states.playerStates;

import java.util.Stack;

public class PlayerStateManager {
    private final Stack<PlayerState> states;

    public PlayerStateManager(){
        this.states = new Stack<>();
    }

    public void set(PlayerState state){
        if (!states.isEmpty()) {
            states.pop();
        }
        states.push(state);
    }

    public void push(PlayerState state) {
        states.push(state);
    }

    public void pop() {
        if (!states.isEmpty()) {
            states.pop();
        }
    }

    public void clear() {
        states.clear();
    }

    public PlayerState checkState(){
        if (states.isEmpty()) return null;
        return this.states.peek();
    }

    public boolean isCanMove(){
        if (states.isEmpty()) return false;
        return states.peek().isCanMove();
    }

    public float getVelocityMul() {
        if (states.isEmpty()) return 1.0f;
        return states.peek().getVelocityMul();
    }

    public float getAtkMul() {
        if (states.isEmpty()) return 1.0f;
        return states.peek().getAtkMul();
    }

    public float getEnergyDrainMul() {
        if (states.isEmpty()) return 1.0f;
        return states.peek().getEnergyDrainMul();
    }

    public float getDamage() {
        if (states.isEmpty()) return 0f;
        return states.peek().getDamage();
    }

    public void update(float delta) {
        if (!states.isEmpty()) {
            PlayerState current = states.peek();
            float duration = current.getDuration();

            if (duration > 0) {
                duration -= delta;
                current.setDuration(duration);

                if (duration <= 0) {
                    pop();
                }
            }
        }
    }
}
