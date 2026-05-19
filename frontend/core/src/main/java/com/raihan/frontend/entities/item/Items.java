package com.raihan.frontend.entities.item;

import com.raihan.frontend.entities.Player;

public abstract class Items {
    protected String name;
    protected Player owner;
    protected float durability = 100f;
    protected float impact;

    protected Items(String name){
        this.name = name;
    }

    public void setOwner(Player owner) { this.owner = owner; }
    public abstract void update(float delta); // for decreasing item's durability
    public float getImpact() { return impact; }
    public Player getOwner(){ return owner; }
    public float getDurability(){ return durability; }
    public String getName() { return name; }
}
