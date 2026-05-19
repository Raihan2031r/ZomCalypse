package com.raihan.frontend.observers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreUIObserver implements Observer {
    private int currentScore = 0;

    private final OrthographicCamera uiCamera;
    private final SpriteBatch batch;
    private final BitmapFont font;

    public ScoreUIObserver() {
        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.setToOrtho(false);

        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.setColor(Color.GOLD);
    }

    @Override
    public void update(int score) {
        this.currentScore = score;
    }

    public void render() {
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);

        batch.begin();
        font.draw(batch, "Total Score: " + currentScore, 20f, 40f);
        batch.end();
    }

    public void resize(int width, int height) {
        uiCamera.setToOrtho(false, width, height);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
