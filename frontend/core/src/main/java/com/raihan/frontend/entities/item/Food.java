package com.raihan.frontend.entities.item;

import com.raihan.frontend.states.playerStates.PoisonedState;

public class Food extends Items implements Consumables{
    public Food(float durability, float impact) {
        super("Food");
        this.durability = durability;
        this.impact = impact;
    }

    @Override
    public void use() {
        float regen = 5f;
        if (durability < 0) {
            regen *= -1;
            owner.setState(new PoisonedState(impact/20f));
        } else owner.takeImpact(regen);

        owner.eat(impact);
    }

    @Override
    public void update(float delta) {
        if (durability > -100f) durability -= 0.3f;
    }
}
