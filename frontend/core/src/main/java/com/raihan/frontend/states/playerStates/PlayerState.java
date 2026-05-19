package com.raihan.frontend.states.playerStates;

public abstract class PlayerState {
    private float duration = 0f;
    private float damage = 0f;
    private boolean canMove = true;
    private float velocityMul = 1f; // multiplier for speed
    private float atkMul = 1f; // multiplier for speed
    private float energyDrainMul = 1f; // multiplier for energy drain (running)

    protected void setDuration(float duration){ this.duration = duration; }
    protected void setDamage(float damage) { this.damage = damage; }
    protected void CanMove(){ this.canMove = true; }
    protected void CannotMove(){ this.canMove = false; }
    protected void setVelocityMul(float velocityMul){ this.velocityMul = velocityMul; }
    protected void setAtkMul(float atkMul){ this.atkMul = atkMul; }
    protected void setEnergyDrainMul(float energyDrainMul){ this.energyDrainMul = energyDrainMul; }

    public float getDuration() { return duration; }
    public float getDamage() { return damage; }
    public boolean isCanMove() {return canMove; }
    public float getVelocityMul() { return velocityMul; }
    public float getAtkMul() { return atkMul; }
    public float getEnergyDrainMul() { return energyDrainMul; }
}
