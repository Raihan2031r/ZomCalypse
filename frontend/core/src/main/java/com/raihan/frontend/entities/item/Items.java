package com.raihan.frontend.entities.item;

import com.raihan.frontend.entities.Player;

public abstract class Items {
    protected String name;
    protected Player owner = null; // if no one held the object, this field will be null
    protected float durability = 100f;
    protected float impact;

    protected Items(String name){
        this.name = name;
    }

    public void setOwner(Player owner) { this.owner = owner; }
    public float getImpact() { return impact; }
    public Player getOwner(){ return owner; }
    public float getDurability(){ return durability; }
    public abstract void Use(Player player);
    public abstract void reduceDurability();
}
