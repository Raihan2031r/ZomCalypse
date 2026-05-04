package com.raihan.frontend.entities;

import com.raihan.frontend.entities.item.Items;
import com.raihan.frontend.entities.item.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final Player owner;
    private final List<Items> items;
    private final int LIMIT = 20;

    public Inventory(Player player){
         this.items = new ArrayList<>();
         this.owner = player;
    }

    public void update(float delta){
        for (Items i: items){
            i.update(delta);
        }
    }

    public void addItem(Items item){
        if(items.size() < LIMIT){
            item.setOwner(owner);
            items.add(item);
        }
    }

    public void dropItem(Items item){
        items.remove(item);
    }

    public int getTotalItems(){
        return items.size();
    }

    public List<Items> getItems() {
        return items;
    }

    public Weapon getWeapon(Weapon weapon){
        if(items.contains((Items) weapon)){
            return weapon;
        }
        else return null;
    }
}
