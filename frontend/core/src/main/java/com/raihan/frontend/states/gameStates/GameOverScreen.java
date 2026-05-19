package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raihan.frontend.GameManager;

public class GameOverScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;

    public GameOverScreen(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage(new ScreenViewport());

        skin = new Skin();

        BitmapFont defaultFont = new BitmapFont();

        skin.add("default-font", defaultFont);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.7f, 0.1f, 0.1f, 1f);
        pixmap.fill();
        skin.add("btn-up", new Texture(pixmap));

        pixmap.setColor(0.9f, 0.2f, 0.2f, 1f);
        pixmap.fill();
        skin.add("btn-down", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE);
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.getDrawable("btn-up");
        btnStyle.down = skin.getDrawable("btn-down");
        btnStyle.font = skin.getFont("default-font");
        skin.add("default", btnStyle);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.getFont("default-font"), Color.RED);
        Label title = new Label("YOU DIED", titleStyle);
        title.setFontScale(3f);
        table.add(title).padBottom(20).row();

        GameManager gm = GameManager.getInstance();
        int days = gm.getScoreManager().getDaysSurvived();
        int kills = gm.getScoreManager().getZombieKills();
        int score = gm.getScoreManager().getTotalScore();

        gm.endGame();

        Label statsLabel = new Label(
            "Days Survived: " + days + "\n" +
                "Zombies Killed: " + kills + "\n" +
                "Final Score: " + score,
            skin
        );
        statsLabel.setAlignment(Align.center);
        table.add(statsLabel).padBottom(40).row();

        TextButton menuBtn = new TextButton("Back to Menu", skin);
        table.add(menuBtn).size(200, 50).padBottom(15).row();

        TextButton exitBtn = new TextButton("Exit Game", skin);
        table.add(exitBtn).size(200, 50);

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gm.getScoreManager().resetScore();
                gsm.set(new MenuScreen(gsm));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.2f, 0.0f, 0.0f, 1f);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override public void update(float delta) {}
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
