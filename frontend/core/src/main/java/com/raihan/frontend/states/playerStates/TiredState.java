package com.raihan.frontend.states.playerStates;

public class TiredState extends PlayerState {
    public TiredState() {
        CanMove();
        setVelocityMul(0.5f);
        setAtkMul(0.5f);
        setEnergyDrainMul(0.5f);
    }
}
