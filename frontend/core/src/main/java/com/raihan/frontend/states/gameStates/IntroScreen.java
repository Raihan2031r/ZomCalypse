package com.raihan.frontend.states.gameStates;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class IntroScreen implements GameScreen {
    private final Texture image;
    private GameStateManager gsm;

    private float alpha = 0f;
    private float timer = 0f;
    private float timerMul = 1.0f;
    private boolean fasten = false;

    public IntroScreen(SpriteBatch batch, GameStateManager gsm) {
        this.image = new Texture("libgdx.png");
        this.gsm = gsm;
    }

    @Override
    public void update(float delta) {
        timer += timerMul * delta;
        if (timer < 1f) {
            alpha = timer;
        } else if (timer < 2f) {
            alpha = 1f;
        } else if (timer < 3f) {
            alpha = 1f - (timer - 2f);
        } else {
            alpha = 0f;
            gsm.set(new MenuScreen(gsm));
        }
        if (alpha < 0f) alpha = 0f;
        if (alpha > 1f) alpha = 1f;

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && fasten == false){
            timerMul *= 5.0f;
            fasten = true;
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, alpha);
        spriteBatch.draw(image, 140, 210);
        spriteBatch.setColor(1, 1, 1, 1);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        image.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
