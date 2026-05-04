package com.raihan.frontend.pools;
import com.badlogic.gdx.math.Vector2;

public class Bullets {
    private float damage;
    private float range;
    private Vector2 velocity;

    private Vector2 position;
    private Vector2 startPosition;
    private boolean active;

    public Bullets() {
        this.velocity = new Vector2();
        this.position = new Vector2();
        this.startPosition = new Vector2();
        this.active = false;
    }

    public void spawn(float startX, float startY, float dirX, float dirY, float speed, float damage, float range) {
        this.damage = damage;
        this.range = range;

        this.position.set(startX, startY);
        this.startPosition.set(startX, startY);

        this.velocity.set(dirX, dirY).nor().scl(speed);
        this.active = true;
    }

    public void update(float delta) {
        if (!active) return;
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        if (position.dst2(startPosition) >= (range * range)) {
            deactivate();
        }
    }

    public void hit() {
        deactivate();
    }

    private void deactivate() {
        this.active = false;
        this.velocity.setZero();
    }

    public boolean isActive() { return active; }
    public Vector2 getPosition() { return position; }
    public float getDamage() { return damage; }
}
