package com.raihan.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.raihan.frontend.states.gameStates.GameStateManager;
import com.raihan.frontend.states.gameStates.IntroScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private IntroScreen intro;
    private GameStateManager gsm;
    private ShapeRenderer renderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        renderer = new ShapeRenderer();

        gsm.push(new IntroScreen(batch, gsm));
    }

    @Override
    public void render(){
        float delta = Gdx.graphics.getDeltaTime();
        gsm.update(delta);
        gsm.render(renderer, batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
