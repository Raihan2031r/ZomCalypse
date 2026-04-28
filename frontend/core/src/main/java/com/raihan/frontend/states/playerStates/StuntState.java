package com.raihan.frontend.states.playerStates;

public class StuntState extends PlayerState {
    public StuntState() {
        CannotMove();
        setDuration(2.0f);
        setVelocityMul(0f);
        setAtkMul(0f);
        setEnergyDrainMul(0f);
    }
}
