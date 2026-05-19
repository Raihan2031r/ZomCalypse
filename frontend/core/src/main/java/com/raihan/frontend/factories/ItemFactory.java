package com.raihan.frontend.factories;

import com.raihan.frontend.entities.item.*;

import java.util.Random;

public class ItemFactory {
    private Random random = new Random();

    public Items getRandomLoot() {
        int rng = random.nextInt(1, 100);

        if (rng <= 30) {
            float durability = random.nextFloat(50f, 100f);
            float impact = random.nextFloat(10f, 25f);
            return new Food(durability, impact);

        } else if (rng <= 60) {
            float durability = random.nextFloat(50f, 100f);
            float impact = random.nextFloat(15f, 30f);
            return new Drink(durability, impact);

        } else if (rng <= 85) {
            return new Ammo();

        } else {
            return new Bandage();
        }
    }

    public Items getTrashLoot() {
        int rng = random.nextInt(1, 100);

        if (rng <= 60) {
            float rottenDurability = random.nextFloat(-80f, -10f);
            float impact = random.nextFloat(5f, 15f);
            return new Food(rottenDurability, impact);
        } else {
            float rottenDurability = random.nextFloat(-80f, -10f);
            float impact = random.nextFloat(5f, 15f);
            return new Drink(rottenDurability, impact);
        }
    }

    public Items getVendingMachineLoot() {
        int rng = random.nextInt(1, 2);
        if (rng == 1) {
            return new Drink(100f, 40f);
        } else {
            return new Food(100f, 30f);
        }
    }
}
