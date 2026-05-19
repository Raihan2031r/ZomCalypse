package com.raihan.frontend.factories;

import com.raihan.frontend.entities.item.*;

import java.util.Random;

public class ItemFactory {
    private Random random = new Random();

    public Items getRandomLoot() {
        int rng = 1 + random.nextInt(100);

        if (rng <= 30) {
            float durability = 50f + random.nextFloat() * 50f;
            float impact = 10f + random.nextFloat() * 15f;
            return new Food(durability, impact);

        } else if (rng <= 60) {
            float durability = 50f + random.nextFloat() * 50f;
            float impact = 15f + random.nextFloat() * 15f;
            return new Drink(durability, impact);

        } else if (rng <= 85) {
            return new Ammo();

        } else {
            return new Bandage();
        }
    }

    public Items getTrashLoot() {
        int rng = 1 + random.nextInt(100);

        if (rng <= 60) {
            float rottenDurability = -80f + random.nextFloat() * 70f;
            float impact = 5f + random.nextFloat() * 10f;
            return new Food(rottenDurability, impact);
        } else {
            float rottenDurability = -80f + random.nextFloat() * 70f;
            float impact = 5f + random.nextFloat() * 10f;
            return new Drink(rottenDurability, impact);
        }
    }

    public Items getVendingMachineLoot() {
        int rng = 1 + random.nextInt(2);
        if (rng == 1) {
            return new Drink(100f, 40f);
        } else {
            return new Food(100f, 30f);
        }
    }
}
