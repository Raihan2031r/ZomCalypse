package com.raihan.frontend.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Zombies extends Enemies{
    public Zombies(float x, float y, float hp, float atk, float speed, float detectRad, float atkRad) {
        super(x, y, hp, atk, speed, detectRad, atkRad);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect(position.x, position.y, collider.width, collider.height);

        float barY = position.y + collider.height + 4f;
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(position.x, barY, collider.width, 3f);

        float hpPercent = HP / maxHP;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x, barY, collider.width * hpPercent, 3f);
    }
}
