package com.raihan.frontend.entities.item;

public class Ammo extends Items implements Consumables{
    public Ammo() {
        super("Ammo");
        super.impact = 30f;
    }

    @Override
    public void use() {
        super.durability = 0f;
    }

    @Override
    public void update(float delta) {
        // Empty since it's not going to rotten or something
    }
}
