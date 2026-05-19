package com.raihan.frontend.entities.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.states.enemyStates.EnemyStateManager;
import com.raihan.frontend.states.enemyStates.IdleState;

import java.util.List;

public abstract class Enemies {
    protected float HP;
    protected float maxHP;
    protected float atk;
    protected float speed;

    protected Vector2 position;
    protected Vector2 velocity;

    protected Circle detectionRadius;
    protected Rectangle attackCollider;
    protected Rectangle collider;

    protected final EnemyStateManager esm;

    public Enemies(float x, float y, float hp, float atk, float speed, float detectRad, float atkRad) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0f, 0f);
        this.HP = hp;
        this.maxHP = hp;
        this.atk = atk;
        this.speed = speed;

        this.collider = new Rectangle(x, y, 16f, 16f);
        this.detectionRadius = new Circle(x, y, detectRad);

        this.attackCollider = new Rectangle(x, y, atkRad, atkRad);

        this.esm = new EnemyStateManager(this);
    }

    public void update(float delta, Player player) {
        esm.update(delta, player);

        //position.x += velocity.x * delta;
        //position.y += velocity.y * delta;

        updateCollider();
    }

    public abstract void render(ShapeRenderer shapeRenderer);

    protected void updateCollider() {
        collider.setPosition(position.x, position.y);

        float centerX = position.x + collider.width / 2f;
        float centerY = position.y + collider.height / 2f;

        detectionRadius.setPosition(centerX, centerY);

        attackCollider.setPosition(centerX - attackCollider.width / 2f, centerY - attackCollider.height / 2f);
    }

    public void spawn(float x, float y, float hp, float atk, float speed) {
        this.position.set(x, y);
        this.velocity.setZero();
        this.HP = hp;
        this.maxHP = hp;
        this.atk = atk;
        this.speed = speed;

        updateCollider();
        this.esm.set(new IdleState());
    }

    public void takeDamage(float damage){
        if(this.HP > 0){
            this.HP -= damage;
            if (this.HP <= 0) {
                this.HP = 0;
            }
        }
    }

    public void moveAndCollide(float delta, List<Rectangle> obstacles) {
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
    }

    public EnemyStateManager getEsm() { return esm; }

    public Circle getDetectionRadius() { return detectionRadius; }

    public Rectangle getAttackCollider() { return attackCollider; }

    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    public float getSpeed() { return speed; }
    public float getAtk() { return atk; }
    public float getHP() { return HP; }

    public Rectangle getCollider() {
        return this.collider;
    }
}
