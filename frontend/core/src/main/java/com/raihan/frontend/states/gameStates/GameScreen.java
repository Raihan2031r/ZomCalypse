package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface GameScreen extends Screen {
    void update(float delta);
    void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch);
    void dispose();
    void resize(int width, int height);
}
