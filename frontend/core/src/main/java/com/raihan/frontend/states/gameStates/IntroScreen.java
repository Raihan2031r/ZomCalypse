package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class IntroScreen implements GameScreen {
    private final Texture image;
    private GameStateManager gsm;

    private float alpha = 0f;
    private float timer = 0f;
    private boolean hold = false;
    private boolean hasHeld = false;

    private OrthographicCamera camera;
    private Viewport viewport;

    public IntroScreen(GameStateManager gsm) {
        this.image = new Texture("intro_screen.png");
        this.gsm = gsm;

        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
    }

    @Override
    public void update(float delta) {
        if (!hold) {
            timer += delta;
        }

        if (timer >= 2f && !hasHeld) {
            hold = true;
            hasHeld = true;
            timer = 2f;
        }

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

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            hold = false;
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        float targetWidth = image.getWidth();
        float targetHeight = image.getHeight();

        if (targetWidth > viewport.getWorldWidth() || targetHeight > viewport.getWorldHeight()) {
            float scale = Math.min(viewport.getWorldWidth() / targetWidth, viewport.getWorldHeight() / targetHeight);
            targetWidth *= scale;
            targetHeight *= scale;
        }

        float x = (viewport.getWorldWidth() - targetWidth) / 2f;
        float y = (viewport.getWorldHeight() - targetHeight) / 2f;

        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, alpha);
        spriteBatch.draw(image, x, y, targetWidth, targetHeight);
        spriteBatch.setColor(1, 1, 1, 1);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        image.dispose();
    }

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
