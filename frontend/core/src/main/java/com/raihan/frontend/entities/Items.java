package com.raihan.frontend.entities;

public interface Items {
    Player owner = null; // if no one held the object, this field will be null
    float durability = 100f;

    public Player getOwner();
    public float getDurability();
    public void Use(Player player);
}
