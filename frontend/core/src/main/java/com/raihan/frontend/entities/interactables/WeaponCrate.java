package com.raihan.frontend.entities.interactables;

import com.badlogic.gdx.math.Rectangle;
import com.raihan.frontend.factories.ItemFactory;
import com.raihan.frontend.observers.ScoreManager;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.item.Rifle;

public class WeaponCrate extends Crates{
    public WeaponCrate(Rectangle bounds, ItemFactory itemFactory) {
        super(bounds, 0f, false, itemFactory);
    }

    @Override
    public void interact(Player player, ScoreManager scoreManager) {
        if (!isLooted){
            player.pickUp(new Rifle());
            cooldownTimer = cooldownDuration;
            isLooted = true;
        }
    }
}
