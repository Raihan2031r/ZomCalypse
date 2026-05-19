package com.raihan.frontend.observers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.raihan.frontend.entities.item.Rifle;
import com.raihan.frontend.entities.item.Weapon;

public class PlayerUIObserver implements PlayerObserver {
    private float hp = 100f, energy = 100f, satiation = 100f, hydration = 100f;
    private Weapon weapon;
    private int ammoCount = 0;

    private final OrthographicCamera uiCamera;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final BitmapFont font;

    public PlayerUIObserver() {
        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.setToOrtho(false);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(1.2f);
    }

    @Override
    public void updateStats(float hp, float energy, float satiation, float hydration, Weapon weapon, int ammoCount) {
        this.hp = hp;
        this.energy = energy;
        this.satiation = satiation;
        this.hydration = hydration;

        this.weapon = weapon;
        this.ammoCount = ammoCount;
    }

    public void resize(int width, float height) {
        uiCamera.setToOrtho(false, width, height);
    }

    public void render(int hour, int minute, float timeRemaining) {
        uiCamera.update();

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float startX = 20f;
        float startY = Gdx.graphics.getHeight() - 30f;
        float barWidth = 150f;
        float barHeight = 15f;
        float spacing = 22f;

        drawBar(shapeRenderer, startX, startY, barWidth, barHeight, hp, Color.FIREBRICK, Color.GREEN); // HP
        drawBar(shapeRenderer, startX, startY - spacing, barWidth, barHeight, energy, Color.DARK_GRAY, Color.YELLOW); // Energy
        drawBar(shapeRenderer, startX, startY - 2 * spacing, barWidth, barHeight, satiation, Color.DARK_GRAY, Color.ORANGE); // Food
        drawBar(shapeRenderer, startX, startY - 3 * spacing, barWidth, barHeight, hydration, Color.DARK_GRAY, Color.CYAN); // Water
        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        font.setColor(Color.WHITE);
        font.draw(batch, "HP", startX + barWidth + 10, startY + 12);
        font.draw(batch, "ENERGY", startX + barWidth + 10, startY - spacing + 12);
        font.draw(batch, "FOOD", startX + barWidth + 10, startY - 2 * spacing + 12);
        font.draw(batch, "WATER", startX + barWidth + 10, startY - 3 * spacing + 12);

        // --- Perbaikan GWT untuk Waktu ---
        String hourStr = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);
        String timeText = "Time: " + hourStr + ":" + minuteStr;

        GlyphLayout layout = new GlyphLayout(font, timeText);
        float timeX = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float timeY = Gdx.graphics.getHeight() - 15f;
        font.draw(batch, timeText, timeX, timeY);

        if (weapon instanceof Rifle) {
            Rifle rifle = (Rifle) weapon;
            int currentMag = rifle.getMagazine();
            int totalReserve = ammoCount * 30;

            String ammoText = currentMag + " / " + totalReserve;

            if (rifle.isReloading()) {
                ammoText = "Reloading...";
            }

            GlyphLayout ammoLayout = new GlyphLayout(font, ammoText);

            float ammoX = Gdx.graphics.getWidth() - ammoLayout.width - 20f;
            float ammoY = 30f;

            font.setColor(Color.YELLOW);
            font.draw(batch, ammoText, ammoX, ammoY);
        }

        int displayMinutes = (int) (timeRemaining / 60);
        int displaySeconds = (int) (timeRemaining % 60);

        float xPosition = Gdx.graphics.getWidth() / 2f - 80f;
        float yPosition = Gdx.graphics.getHeight() - 40f;

        String waveMinStr = displayMinutes < 10 ? "0" + displayMinutes : String.valueOf(displayMinutes);
        String waveSecStr = displaySeconds < 10 ? "0" + displaySeconds : String.valueOf(displaySeconds);
        String waveText = "Next Wave: " + waveMinStr + ":" + waveSecStr;

        font.setColor(Color.ORANGE);
        font.draw(batch, waveText, xPosition, yPosition);

        font.setColor(Color.WHITE);

        batch.end();
    }

    private void drawBar(ShapeRenderer sr, float x, float y, float width, float height, float value, Color bg, Color fg) {
        sr.setColor(bg);
        sr.rect(x, y, width, height);

        sr.setColor(fg);
        float percent = Math.max(0, Math.min(100f, value)) / 100f;
        sr.rect(x, y, width * percent, height);
    }

    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
