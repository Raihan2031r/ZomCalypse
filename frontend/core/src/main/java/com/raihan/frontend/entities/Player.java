package com.raihan.frontend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.raihan.frontend.animations.AnimationStrategy;
import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.entities.item.Items;
import com.raihan.frontend.entities.item.Rifle;
import com.raihan.frontend.entities.item.Weapon;
import com.raihan.frontend.factories.BulletFactory;
import com.raihan.frontend.observers.PlayerObserver;
import com.raihan.frontend.observers.PlayerSubject;
import com.raihan.frontend.states.playerStates.*;

import java.util.ArrayList;
import java.util.List;

public class Player implements PlayerSubject {
    private String name;
    private float HP = 100f;
    private Weapon weapon;
    private float energy = 100f;
    private float satiation = 100f;
    private float hydration = 100f;

    private Vector2 velocity;
    private Vector2 position;
    private Rectangle collider;
    private Circle detectionRadius;
    private Rectangle attackCollider;
    private final float WIDTH = 16f;
    private final float HEIGHT = 16f;
    private final float HUNGRY_THRESHOLD = 30f;
    private final float THIRST_THRESHOLD = 40f;
    private Inventory inventory;

    private float baseSpeed = 200f;
    private float accelerationRate = 1200f;
    private float frictionRate = 1000f;
    private float currentLimit = 200f;

    private final PlayerStateManager psm;
    private Arah arah;
    private float facingX = 0f;
    private float facingY = -1f;
    private float inputX = 0f;
    private float inputY = 0f;
    private float pressDelay = 0f;
    private float recoveryDelay = 0f;
    float attackDelay;

    private AnimationStrategy animationStrategy;
    private float stateTime = 0f;
    private final List<PlayerObserver> observers = new ArrayList<>();
    private BitmapFont font;
    private String pickupMessage = "";
    private Color pickupColor = Color.WHITE;
    private float pickupTimer = 0f;

    public Player(String name, Weapon weapon, float x, float y){
        this.name = name;
        this.velocity = new Vector2(0f, 0f);
        this.position = new Vector2(x, y);
        this.weapon = weapon;
        this.collider = new Rectangle(x, y, WIDTH, HEIGHT);
        this.detectionRadius = new Circle(x, y, WIDTH);
        this.attackCollider = new Rectangle(position.x, position.y, weapon.getRange(), HEIGHT);
        this.inventory = new Inventory(this);
        this.psm = new PlayerStateManager();
        this.animationStrategy = new AnimationStrategy();
        this.font = new BitmapFont();
        this.font.getData().setScale(0.6f);


        psm.push(new NormalState());
        this.arah = new Arah(false, false, false, false);
    }

    public void render(ShapeRenderer shapeRenderer){
//        shapeRenderer.setColor(Color.ORANGE);
//        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(attackCollider.x, attackCollider.y, attackCollider.width, attackCollider.height);
    }

    public void update(float delta){
        this.stateTime += delta;
        if(attackDelay > 0) this.attackDelay -= delta;
        psm.update(delta);
        if(recoveryDelay > 0f) recoveryDelay -= delta;

        if (attackDelay > 0){
            // Don't update facingX or Y before attack finish
        }
        else if (inputX != 0f && inputY != 0f) {
            facingX = inputX;
            facingY = inputY;
            pressDelay = 0.08f;
        }
        else if (inputX != 0f || inputY != 0f) {
            if (pressDelay > 0f) {
                pressDelay -= delta;
            } else {
                facingX = inputX;
                facingY = inputY;
            }
        }
        else {
            pressDelay = 0f;
        }

        float drainMul = psm.getEnergyDrainMul();
        satiation -= 0.075f * delta * drainMul;
        hydration -= 0.1f * delta * drainMul;
        inventory.update(delta);
        energyRegen(delta);
        recovery(delta);

        checkAutoStates();

        if (psm.checkState() instanceof NormalState || psm.checkState() instanceof IdleState) {
            if (inputX == 0f && inputY == 0f && !(psm.checkState() instanceof IdleState)) {
                psm.set(new IdleState());
            } else if ((inputX != 0f || inputY != 0f) && !(psm.checkState() instanceof NormalState)) {
                psm.set(new NormalState());
            }
        }

        if (psm.isCanMove()) {
            float friction = frictionRate * delta;
            float targetMaxSpeed = baseSpeed * psm.getVelocityMul();

            if (currentLimit < targetMaxSpeed) {
                currentLimit = targetMaxSpeed;
            } else if (currentLimit > targetMaxSpeed) {
                currentLimit -= friction;
                if (currentLimit < targetMaxSpeed) currentLimit = targetMaxSpeed;
            }

            if (!arah.kanan && !arah.kiri) {
                if (velocity.x > 0) velocity.x = Math.max(0, velocity.x - friction);
                else if (velocity.x < 0) velocity.x = Math.min(0, velocity.x + friction);
            }

            if (!arah.atas && !arah.bawah) {
                if (velocity.y > 0) velocity.y = Math.max(0, velocity.y - friction);
                else if (velocity.y < 0) velocity.y = Math.min(0, velocity.y + friction);
            }

            if (velocity.len() > currentLimit) {
                velocity.setLength(currentLimit);
            }

            //position.x += velocity.x * delta;
            //position.y += velocity.y * delta;
        } else {
            velocity.setZero();
        }
        //System.out.println("X: " + facingX);
        //System.out.println("Y: " + facingY);

        damageOverTime(psm.getDamage(), delta);
        updateCollider();
        if (pickupTimer > 0) pickupTimer -= delta;

        notifyObservers();
    }

    public void renderTexture(SpriteBatch batch) {
        boolean isShooting = this.attackDelay > 0;
        boolean isReloading = this.weapon.isReloading();

        TextureRegion currentFrame = animationStrategy.getCurrentFrame(
            psm.checkState(),
            this.weapon,
            this.velocity,
            isShooting,
            isReloading,
            this.stateTime,
            this.facingX,
            this.facingY
        );

        if (currentFrame != null) {
            float spriteWidth = 48f;
            float spriteHeight = 64f;

            float offsetX = (spriteWidth - this.collider.width) / 2f;
            float offsetY = (spriteHeight - this.collider.height) / 2f;

            batch.draw(currentFrame, this.collider.x - offsetX, this.collider.y - offsetY, spriteWidth, spriteHeight);
        }

        font.setColor(Color.WHITE);
        font.draw(batch, name, position.x, position.y + 2 * HEIGHT);

        if (pickupTimer > 0) {
            float alpha = Math.min(1f, pickupTimer);
            font.setColor(pickupColor.r, pickupColor.g, pickupColor.b, alpha);

            float floatY = (2f - pickupTimer) * 15f;

            font.draw(batch, pickupMessage, position.x + WIDTH, position.y + HEIGHT + floatY);
        }
    }

    private void updateCollider() {
        collider.setPosition(position);

        float centerX = position.x + collider.width / 2f;
        float centerY = position.y + collider.height / 2f;

        detectionRadius.setPosition(centerX, centerY);
    }

    private void updateAttackCollider() {
        if (weapon == null) return;

        float range = weapon.getRange();
        float thickness = WIDTH;

        float playerCenterX = collider.x + collider.width / 2f;
        float playerCenterY = collider.y + collider.height / 2f;

        float ax = playerCenterX;
        float ay = playerCenterY;
        float aw = thickness;
        float ah = thickness;

        if (facingX > 0 && facingY == 0) {
            ax = collider.x + collider.width;
            ay = playerCenterY - thickness / 2f;
            aw = range;
            ah = thickness;
        } else if (facingX < 0 && facingY == 0) {
            ax = collider.x - range;
            ay = playerCenterY - thickness / 2f;
            aw = range;
            ah = thickness;
        } else if (facingY > 0 && facingX == 0) {
            ax = playerCenterX - thickness / 2f;
            ay = collider.y + collider.height;
            aw = thickness;
            ah = range;
        } else if (facingY < 0 && facingX == 0) {
            ax = playerCenterX - thickness / 2f;
            ay = collider.y - range;
            aw = thickness;
            ah = range;
        } else {
            aw = range;
            ah = range;
            ax = (facingX > 0) ? collider.x + collider.width : collider.x - range;
            ay = (facingY > 0) ? collider.y + collider.height : collider.y - range;
        }

        attackCollider.set(ax, ay, aw, ah);
    }

    public void moveAndCollide(float delta, List<Rectangle> obstacles) {
        if(psm.checkState() instanceof DyingState) return;
        float oldX = position.x;
        float oldY = position.y;

        position.x += velocity.x * delta;
        updateCollider();

        for (Rectangle wall : obstacles) {
            if (this.collider.overlaps(wall)) {
                position.x = oldX;
                velocity.x = 0;
                updateCollider();
                break;
            }
        }

        position.y += velocity.y * delta;
        updateCollider();

        for (Rectangle wall : obstacles) {
            if (this.collider.overlaps(wall)) {
                position.y = oldY;
                velocity.y = 0;
                updateCollider();
                break;
            }
        }

        if (!(velocity.isZero())) energy -= 3 * delta * psm.getEnergyDrainMul();
    }

    private void energyRegen(float delta) {
        if (psm.checkState() instanceof DyingState || psm.checkState() instanceof RunningState) return;
        if (energy < 100f) {
            if(velocity.x != 0f || velocity.y != 0f){
                energy += 5.0f * delta;
            } else {
                energy += 20.0f * delta;
            }
        }
        if (energy > 100f) energy = 100f;
        if (energy > 20f && psm.checkState() instanceof TiredState) psm.pop();
    }

    private void recovery(float delta) {
        if (psm.checkState() instanceof DyingState || psm.checkState() instanceof HungryState) return;
        if(recoveryDelay <= 0f && HP < 100f){
            if(velocity.x != 0f || velocity.y != 0f ) HP += 2.0f * delta;
            else HP += 5.0f * delta;

            if(HP > 100f) HP = 100f;
        }

        if(
            (satiation > HUNGRY_THRESHOLD && psm.checkState() instanceof HungryState)
                ||
            (hydration > THIRST_THRESHOLD && psm.checkState() instanceof ThirstState)
        ) psm.pop();
    }

    private void checkAutoStates() {
        if (HP <= 0 && !(psm.checkState() instanceof DyingState)) {
            psm.set(new DyingState());
        }
        if (satiation <= HUNGRY_THRESHOLD &&
            !(psm.checkState() instanceof HungryState) &&
            !(psm.checkState() instanceof DyingState))
            psm.push(new HungryState());
        if (hydration <= THIRST_THRESHOLD &&
            !(psm.checkState() instanceof ThirstState)&&
            !(psm.checkState() instanceof DyingState))
            psm.push(new ThirstState());
        if (energy <= 0 && !(psm.checkState() instanceof TiredState) &&
            !(psm.checkState() instanceof DyingState)){
            psm.push(new TiredState());
            if (psm.checkState() instanceof RunningState) stopRun();
        }
        if (
            !(
                psm.checkState() instanceof RunningState ||
                psm.checkState() instanceof TiredState ||
                psm.checkState() instanceof DyingState ||
                psm.checkState() instanceof ThirstState ||
                psm.checkState() instanceof HungryState ||
                psm.checkState() instanceof IdleState ||
                psm.checkState() instanceof NormalState
            )
        ) psm.set(new NormalState());
    }

    public void run(){
        if(psm.isCanMove() && energy > 30 && !(psm.checkState() instanceof RunningState)){
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
        inputX = 0f;
        inputY = 0f;
    }

    public void moveUp(){
        if(psm.isCanMove()){
            arah.atas = true;
            arah.bawah = false;
            inputY = 1f;
            velocity.y += accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveDown(){
        if(psm.isCanMove()){
            arah.bawah = true;
            arah.atas = false;
            inputY = -1f;
            velocity.y -= accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveRight(){
        if(psm.isCanMove()){
            arah.kanan = true;
            arah.kiri = false;
            inputX = 1f;
            velocity.x += accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void moveLeft(){
        if(psm.isCanMove()){
            arah.kiri = true;
            arah.kanan = false;
            inputX = -1f;
            velocity.x -= accelerationRate * Gdx.graphics.getDeltaTime();
        }
    }

    public void takeImpact(float impact){
        if(this.HP > 0){
            this.HP += impact;
            if (this.HP < 0) this.HP = 0;
            recoveryDelay = 5f;
        }
    }

    private void damageOverTime(float damage, float delta){
        if (damage > 0f) {
            if(HP > 0) this.HP -= damage * delta;
            recoveryDelay = 5f;
        }
    }

    public void eat(float impact){
        this.satiation += impact;
        if (satiation > 100) satiation = 100;
    }

    public void drink(float impact){
        this.hydration += impact;
        if (hydration > 100) hydration = 100;
    }

    public void pickUp(Items item){
        inventory.addItem(item);

        pickupMessage = "+ " + item.getName();
        pickupColor = getColorByItemType(item);
        pickupTimer = 2.0f;
    }

    private Color getColorByItemType(Items item) {
        if (item.getName().equals("Food")) return Color.GREEN;
        if (item.getName().equals("Drink")) return Color.CYAN;
        if (item.getName().equals("Ammo")) return Color.YELLOW;
        if (item.getName().equals("Bandage")) return Color.RED;
        if (item instanceof Weapon) return Color.GRAY;
        return Color.WHITE;
    }

    public void drop(Items item){
        inventory.dropItem(item);
    }

    public void changeWeapon(Weapon weapon){
        Weapon newWeapon = inventory.getWeapon(weapon);
        if (newWeapon != null) this.weapon = weapon;
    }

    public void attack(List<Enemies> enemies){
        boolean attackActive;
        if(attackDelay > 0) return;
        else attackActive = true;

        weapon.Attack();
        updateAttackCollider();
        attackDelay = weapon.getAttackDelay();
        for (Enemies enemy : enemies) {
            if (attackCollider.overlaps(enemy.getCollider()) && attackActive) {
                enemy.takeDamage(weapon.getDamage());
                attackActive = false;
            }
        }
    }

    public void attack(BulletFactory bulletFactory){
        Rifle rifle;
        if (weapon instanceof Rifle) {
            rifle = (Rifle) weapon;
            if(attackDelay > 0 || rifle.getMagazine() <= 0) return;
            if(rifle.getDurability() > 0 && !(rifle.isReloading())){
                rifle.Attack(bulletFactory);
                attackDelay = rifle.getAttackDelay();
            }
        }

    }

    public String getName() { return name; }
    public Circle getDetectionRadius() { return detectionRadius; }
    public PlayerState getState(){ return psm.checkState(); }
    public Arah getArah() { return arah; }
    public Rectangle getCollider() { return this.collider; }
    public Vector2 getPosition() { return position; }
    public List<Items> getItems() { return inventory.getItems(); }
    public Weapon getWeapon() { return weapon; }
    public Vector2 getDirection() { return new Vector2(facingX, facingY); }

    public void setState(PlayerState state) {
        psm.set(state);
    }

    public void setStatsFromSave(float hp, float energy, float satiation, float hydration) {
        this.HP = hp;
        this.energy = energy;
        this.satiation = satiation;
        this.hydration = hydration;
    }

    public void dispose(){
        animationStrategy.dispose();
        if (font != null) font.dispose();
    }

    public float getAttackDelay() {
        return attackDelay;
    }

    public void stopMoving(){
        velocity.setZero();
    }

    @Override
    public void addObserver(PlayerObserver observer) { observers.add(observer); }

    @Override
    public void removeObserver(PlayerObserver observer) { observers.remove(observer); }

    @Override
    public void notifyObservers() {
        int ammoCount = inventory.countAmmo();

        for (PlayerObserver obs : observers) {
            obs.updateStats(HP, energy, satiation, hydration, weapon, ammoCount);
        }
    }

    public Inventory getInventoryObject() {
        return inventory;
    }

    public float getHP() {
        return HP;
    }

    public float getEnergy() {
        return energy;
    }

    public float getSatiation() {
        return satiation;
    }

    public float getHydration() {
        return hydration;
    }
}
