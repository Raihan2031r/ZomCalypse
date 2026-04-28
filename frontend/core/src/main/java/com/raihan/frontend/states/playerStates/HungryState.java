package com.raihan.frontend.states.playerStates;

public class HungryState extends PlayerState {
    public HungryState() {
        CanMove();
        setDamage(1.0f);
        setVelocityMul(0.8f);
        setAtkMul(0.7f);
        setEnergyDrainMul(1.0f);
    }
}
