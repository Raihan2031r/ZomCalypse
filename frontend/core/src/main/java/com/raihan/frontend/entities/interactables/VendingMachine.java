package com.raihan.frontend.entities.interactables;

import com.badlogic.gdx.math.Rectangle;
import com.raihan.frontend.factories.ItemFactory;
import com.raihan.frontend.observers.ScoreManager;
import com.raihan.frontend.entities.Player;

public class VendingMachine extends Crates {
    private final int COST = 50;

    public VendingMachine(Rectangle bounds, ItemFactory itemFactory) {
        super(bounds, 1.5f, true, itemFactory);
    }

    @Override
    public void interact(Player player, ScoreManager scoreManager) {
        if (scoreManager.getTotalScore() >= COST) {
            scoreManager.spendScore(COST);
            player.pickUp(itemFactory.getVendingMachineLoot());
            cooldownTimer = cooldownDuration;
            isLooted = true;
        }
    }
}
