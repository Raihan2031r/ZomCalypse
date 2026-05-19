package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;

    public SettingScreen(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage(new ScreenViewport());

        skin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.WHITE;

        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();

        skin.add("default-font", customFont);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.2f, 0.2f, 1f);
        pixmap.fill();
        skin.add("bg-slot", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE);
        skin.add("default", labelStyle);

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new Image(createColorPixmap(0.1f, 0.1f, 0.15f, 1f)).getDrawable());
        stage.addActor(root);

        Label titleLabel = new Label("SETTING", skin);
        titleLabel.setFontScale(1.5f);
        root.add(titleLabel).padBottom(40).colspan(2).row();

        Label resLabel = new Label("Select Window Resolution:", skin);
        root.add(resLabel).padBottom(10).colspan(2).row();

        Table resTable = new Table();
        TextButton res1Btn = createButton("1280 x 720");
        TextButton res2Btn = createButton("1600 x 900");
        TextButton res3Btn = createButton("1920 x 1080");

        resTable.add(res1Btn).size(150, 40).padRight(10);
        resTable.add(res2Btn).size(150, 40).padRight(10);
        resTable.add(res3Btn).size(150, 40);

        root.add(resTable).padBottom(30).colspan(2).row();

        res1Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
        });
        res2Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1600, 900);
            }
        });
        res3Btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1920, 1080);
            }
        });

        String fsText = Gdx.graphics.isFullscreen() ? "Turn Off Fullscreen" : "Turn On Fullscreen";
        TextButton fullscreenBtn = createButton(fsText);
        root.add(fullscreenBtn).size(300, 50).padBottom(40).colspan(2).row();

        fullscreenBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(1280, 720);
                    fullscreenBtn.setText("Turn On Fullscreen");
                } else {
                    DisplayMode displayMode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(displayMode);
                    fullscreenBtn.setText("Turn Off Fullscreen");
                }
            }
        });

        TextButton backBtn = createButton("Back to Menu");
        root.add(backBtn).size(200, 50).colspan(2).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuScreen(gsm));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private TextButton createButton(String text) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default-font");
        style.up = skin.getDrawable("bg-slot");
        return new TextButton(text, style);
    }

    private Texture createColorPixmap(float r, float g, float b, float a) {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(r, g, b, a);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
