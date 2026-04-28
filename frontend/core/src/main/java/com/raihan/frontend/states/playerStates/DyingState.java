package com.raihan.frontend.states.playerStates;

public class DyingState extends PlayerState {
    public DyingState(){
        CannotMove();
        setVelocityMul(0f);
        setAtkMul(0f);
        setEnergyDrainMul(0f);
    }
}
