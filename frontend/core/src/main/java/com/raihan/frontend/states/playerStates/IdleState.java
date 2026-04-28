package com.raihan.frontend.states.playerStates;

public class IdleState extends PlayerState{
    public IdleState(){
        CanMove();
        setVelocityMul(1.0f);
        setAtkMul(1.0f);
        setEnergyDrainMul(0f);
    }
}
