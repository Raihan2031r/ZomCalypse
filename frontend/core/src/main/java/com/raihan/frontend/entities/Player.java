package com.raihan.frontend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.raihan.frontend.states.playerStates.*;

public class Player {
    private String name;
    private float HP = 100f;
    private Weapon weapon;
    private float energy = 100f;
    private float satiation = 100f;
    private float hydration = 100f;

    private Vector2 velocity;
    private Vector2 position;
    private Rectangle collider; // collider for in game object such as buildings
    private Circle detectionRadius; // hit and detected by enemy
    private Circle attackRadius;
    private final float WIDTH = 50f;
    private final float HEIGHT = 50f;

    private float baseSpeed = 200f;
    private float accelerationRate = 1200f;
    private float frictionRate = 1000f;
    private float currentLimit = 200f;

    private final PlayerStateManager psm;
    private Arah arah;

    public Player(String name, Weapon weapon, float x, float y){
        this.name = name;
        this.velocity = new Vector2(0f, 0f);
        this.position = new Vector2(x, y);
        this.weapon = weapon;
        this.collider = new Rectangle(x, y, WIDTH, HEIGHT);
        this.detectionRadius = new Circle(x, y, WIDTH);
        this.attackRadius = new Circle(x,y, weapon.range);
        this.psm = new PlayerStateManager();

        psm.push(new NormalState());
        this.arah = new Arah(false, false, false, false);
    }

    public void render(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
    }

    public void update(float delta){
        psm.update(delta);

        float drainMul = psm.getEnergyDrainMul();
        satiation -= 1.0f * delta * drainMul;
        hydration -= 1.5f * delta * drainMul;
        energyRegen(delta);

        checkAutoStates();

        if (psm.isCanMove()) {
            float friction = frictionRate * delta;
            float targetMaxSpeed = baseSpeed * psm.getVelocityMul();

            if (currentLimit < targetMaxSpeed) {
                currentLimit = targetMaxSpeed;
            } else if (currentLimit > targetMaxSpeed) {
                currentLimit -= friction;
                if (currentLimit < targetMaxSpeed) currentLimit = targetMaxSpeed;
            }

            if (!arah.atas && !arah.bawah) {
                if (velocity.y > 0) velocity.y = Math.max(0, velocity.y - friction);
                else if (velocity.y < 0) velocity.y = Math.min(0, velocity.y + friction);
            }

            if (!arah.kanan && !arah.kiri) {
                if (velocity.x > 0) velocity.x = Math.max(0, velocity.x - friction);
                else if (velocity.x < 0) velocity.x = Math.min(0, velocity.x + friction);
            }

            if (velocity.len() > currentLimit) {
                velocity.setLength(currentLimit);
            }

            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
        } else {
            velocity.setZero();
        }

        updateCollider();
    }

    public void updateCollider() {
        collider.setPosition(position.x, position.y);
    }

    private void energyRegen(float delta) {
        if (!(psm.checkState() instanceof RunningState) && energy < 100f) {
            energy += 15.0f * delta;
            if (energy > 100f) energy = 100f;
        }
    }

    private void checkAutoStates() {
        if (HP <= 0 && !(psm.checkState() instanceof DyingState)) {
            psm.set(new DyingState());
        }
        else if (satiation <= 50 && !(psm.checkState() instanceof HungryState)) {
            psm.push(new HungryState());
        }
        else if (energy <= 10 && !(psm.checkState() instanceof TiredState)){
            psm.push(new TiredState());
            if (psm.checkState() instanceof RunningState) stopRun();
        } else if(!arah.bawah && !arah.atas && !arah.kanan && !arah.kiri && !(psm.checkState() instanceof TiredState || psm.checkState() instanceof HungryState || psm.checkState() instanceof DyingState)) psm.set(new IdleState());
        else if (!(psm.checkState() instanceof RunningState || psm.checkState() instanceof TiredState || psm.checkState() instanceof DyingState || psm.checkState() instanceof HungryState)) psm.set(new NormalState());
        System.out.println(psm.checkState());
    }

    public void run(){
        if(psm.isCanMove() && energy > 0 && !(psm.checkState() instanceof RunningState)){
            psm.push(new RunningState());
        }
    }

    public void stopRun() {
        if (psm.checkState() instanceof RunningState) {
            psm.pop();
        }
    }

    public void idle() {
        arah.atas = false;
        arah.bawah = false;
        arah.kanan = false;
        arah.kiri = false;
    }

    public void moveUp(){
        if(psm.isCanMove()){
            arah.atas = true;
            arah.bawah = false;
            velocity.y += accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveDown(){
        if(psm.isCanMove()){
            arah.bawah = true;
            arah.atas = false;
            velocity.y -= accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveRight(){
        if(psm.isCanMove()){
            arah.kanan = true;
            arah.kiri = false;
            velocity.x += accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveLeft(){
        if(psm.isCanMove()){
            arah.kiri = true;
            arah.kanan = false;
            velocity.x -= accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void takeDamage(float damage){
        if(this.HP > 0){
            this.HP -= damage;
            if (this.HP < 0) this.HP = 0;
        }
    }

    public void attack(Enemies enemy){
        if(weapon.getDamage() > 0 && attackRadius.overlaps(enemy.getDetectionRadius())){
            enemy.takeDamage(weapon.getDamage());
        }
    }

    public PlayerState getState(){ return psm.checkState(); }
    public Arah getArah() { return arah; }
    public Rectangle getCollider() { return this.collider; }
    public Vector2 getPosition() { return position; }
}
