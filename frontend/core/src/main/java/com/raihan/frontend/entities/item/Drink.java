package com.raihan.frontend.entities.item;

import com.raihan.frontend.states.playerStates.PoisonedState;

public class Drink extends Items implements Consumables{
    public Drink(float durability, float impact) {
        super("Drink");
        this.durability = durability;
        this.impact = impact;
    }

    @Override
    public void use() {
        if (durability < 0) owner.setState(new PoisonedState(impact/20f));
        owner.drink(impact);
    }

    @Override
    public void update(float delta) {
        if (durability > -100f) durability -= 0.3f;
    }
}
