package com.raihan.frontend.states.playerStates;

public class RunningState extends PlayerState {
    public RunningState() {
        CanMove();
        setVelocityMul(2.0f);
        setAtkMul(1.0f);
        setEnergyDrainMul(2.0f);
    }
}
