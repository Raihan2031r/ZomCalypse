package com.raihan.frontend.states.playerStates;

public class PoisonedState extends PlayerState {
    public PoisonedState(float duration){
        CanMove();
        setVelocityMul(0.7f);
        setAtkMul(0.7f);
        setEnergyDrainMul(0f);
        setDamage(-5f);
        setDuration(duration);
    }
}
