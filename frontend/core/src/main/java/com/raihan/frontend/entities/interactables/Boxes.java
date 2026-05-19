package com.raihan.frontend.entities.interactables;

import com.badlogic.gdx.math.Rectangle;
import com.raihan.frontend.factories.ItemFactory;
import com.raihan.frontend.observers.ScoreManager;
import com.raihan.frontend.entities.Player;

public class Boxes extends Crates {
    public Boxes(Rectangle bounds, ItemFactory itemFactory) {
        super(bounds, 0.5f, true, itemFactory);
    }

    @Override
    public void interact(Player player, ScoreManager scoreManager) {
        if (isLooted) return;

        player.pickUp(itemFactory.getRandomLoot());
        isLooted = true;
    }
}
