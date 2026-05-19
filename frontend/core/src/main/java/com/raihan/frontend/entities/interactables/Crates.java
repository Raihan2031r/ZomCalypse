package com.raihan.frontend.entities.interactables;

import com.badlogic.gdx.math.Rectangle;
import com.raihan.frontend.factories.ItemFactory;
import com.raihan.frontend.observers.ScoreManager;
import com.raihan.frontend.entities.Player;

public abstract class Crates {
    protected Rectangle bounds;
    protected boolean isLooted;

    protected float cooldownTimer;
    protected float cooldownDuration;
    protected boolean canRespawn;
    protected final ItemFactory itemFactory;

    public Crates(Rectangle bounds, float cooldownMinutes, boolean canRespawn, ItemFactory itemFactory) {
        this.bounds = bounds;
        this.itemFactory = itemFactory;
        this.isLooted = false;
        this.cooldownTimer = 0f;
        this.cooldownDuration = cooldownMinutes * 60f;
        this.canRespawn = canRespawn;
    }

    public Rectangle getBounds() { return bounds; }
    public boolean isLooted() { return isLooted; }

    public void update(float delta) {
        if (isLooted && canRespawn) {
            cooldownTimer -= delta;

            if (cooldownTimer <= 0) {
                isLooted = false;
                cooldownTimer = cooldownDuration;
            }

        }
    }

    public abstract void interact(Player player, ScoreManager scoreManager);
}
