package com.raihan.frontend.states.playerStates;

public class ThirstState extends PlayerState{
    public ThirstState() {
        CanMove();
        setDamage(1.0f);
        setVelocityMul(0.5f);
        setAtkMul(0.6f);
        setEnergyDrainMul(1.0f);
    }
}
