package com.raihan.frontend.entities.item;

public class Bandage extends Items implements  Consumables{
    public Bandage() {
        super("Bandage");
        impact = 5f;
    }

    @Override
    public void use() {
        owner.takeImpact(impact);
    }

    @Override
    public void update(float delta) {
        // NOT EXPIRED
    }
}
