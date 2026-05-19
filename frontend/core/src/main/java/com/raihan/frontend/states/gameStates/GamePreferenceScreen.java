package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
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
import com.raihan.frontend.strategies.EasyMode;
import com.raihan.frontend.strategies.HardMode;
import com.raihan.frontend.strategies.MediumMode;

public class GamePreferenceScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;

    public GamePreferenceScreen(GameStateManager gsm) {
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

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new Image(createColorPixmap(0.1f, 0.1f, 0.15f, 1f)).getDrawable());
        stage.addActor(root);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.3f, 0.3f, 1f);
        pixmap.fill();
        skin.add("btn-up", new Texture(pixmap));

        pixmap.setColor(0.5f, 0.5f, 0.5f, 1f);
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

        Label title = new Label("SELECT DIFFICULTY", skin);
        title.setFontScale(2f);
        table.add(title).padBottom(40).row();

        TextButton easyBtn = new TextButton("Easy Mode", skin);
        TextButton mediumBtn = new TextButton("Medium Mode", skin);
        TextButton hardBtn = new TextButton("Hard Mode", skin);
        TextButton backBtn = new TextButton("Back to Menu", skin);

        float btnWidth = 250f;
        float btnHeight = 50f;
        float padding = 10f;

        table.add(easyBtn).size(btnWidth, btnHeight).pad(padding).row();
        table.add(mediumBtn).size(btnWidth, btnHeight).pad(padding).row();
        table.add(hardBtn).size(btnWidth, btnHeight).pad(padding).row();
        table.add(backBtn).size(btnWidth, btnHeight).padTop(30).row();

        easyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new PlayingScreen(gsm, new EasyMode()));
            }
        });

        mediumBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new PlayingScreen(gsm, new MediumMode()));
            }
        });

        hardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new PlayingScreen(gsm, new HardMode()));
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuScreen(gsm));
            }
        });

        Gdx.input.setInputProcessor(stage);
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
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override public void update(float delta) {}
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
        if (Gdx.input.getInputProcessor() == stage) Gdx.input.setInputProcessor(null);
    }
    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
