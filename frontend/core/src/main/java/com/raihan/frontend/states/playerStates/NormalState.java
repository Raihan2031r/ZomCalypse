package com.raihan.frontend.states.playerStates;

public class NormalState extends PlayerState {
    public NormalState(){
        CanMove();
        setVelocityMul(1.0f);
        setAtkMul(1.0f);
        setEnergyDrainMul(1.0f);
    }
}
