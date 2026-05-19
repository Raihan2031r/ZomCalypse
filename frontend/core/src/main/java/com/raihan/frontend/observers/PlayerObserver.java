package com.raihan.frontend.observers;

import com.raihan.frontend.entities.item.Weapon;

public interface PlayerObserver {
    public abstract void updateStats(float hp, float energy, float satiation, float hydration, Weapon weapon, int ammoCount);
}
