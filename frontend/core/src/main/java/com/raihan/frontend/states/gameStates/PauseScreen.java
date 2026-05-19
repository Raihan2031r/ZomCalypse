package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.raihan.frontend.GameManager;
import com.raihan.frontend.save.SaveDTO;
import com.raihan.frontend.save.SaveManager;
import com.raihan.frontend.services.BackendService;

public class PauseScreen implements GameScreen {
    private final GameStateManager gsm;
    private final GameScreen previousScreen;

    private final Stage stage;
    private final Skin skin;
    private final Texture bgTexture;

    public PauseScreen(GameStateManager gsm, GameScreen previousScreen) {
        this.gsm = gsm;
        this.previousScreen = previousScreen;
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
        pixmap.setColor(0.3f, 0.3f, 0.3f, 1f);
        pixmap.fill();
        skin.add("btn-up", new Texture(pixmap));

        pixmap.setColor(0.5f, 0.5f, 0.5f, 1f);
        pixmap.fill();
        skin.add("btn-down", new Texture(pixmap));

        pixmap.setColor(0f, 0f, 0f, 0.85f);
        pixmap.fill();
        bgTexture = new Texture(pixmap);
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.getDrawable("btn-up");
        btnStyle.down = skin.getDrawable("btn-down");
        btnStyle.font = skin.getFont("default-font");
        skin.add("default", btnStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new Image(bgTexture).getDrawable());
        stage.addActor(table);

        Label title = new Label("GAME PAUSED", skin);
        title.setFontScale(2f);
        table.add(title).padBottom(40).row();

        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton saveBtn = new TextButton("Save Game", skin);
        TextButton menuBtn = new TextButton("Main Menu", skin);
        TextButton exitBtn = new TextButton("Exit Desktop", skin);

        float btnWidth = 200f;
        float btnHeight = 50f;
        float padding = 10f;

        table.add(resumeBtn).size(btnWidth, btnHeight).pad(padding).row();
        table.add(saveBtn).size(btnWidth, btnHeight).pad(padding).row();
        table.add(menuBtn).size(btnWidth, btnHeight).pad(padding).row();
        table.add(exitBtn).size(btnWidth, btnHeight).pad(padding).row();

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.pop();
            }
        });

        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previousScreen instanceof PlayingScreen) {
                    saveBtn.setText("Saving...");

                    PlayingScreen game = (PlayingScreen) previousScreen;
                    SaveDTO data = game.getSaveData();

                    SaveManager.save(data);

                    GameManager.getInstance().saveGameToCloud(data, new BackendService.RequestCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Gdx.app.postRunnable(() -> saveBtn.setText("Save Success!"));
                        }

                        @Override
                        public void onError(String error) {
                            Gdx.app.postRunnable(() -> {
                                if (error.equals("Not logged in")) {
                                    saveBtn.setText("Saved Locally (Not Logged In)");
                                } else {
                                    saveBtn.setText("Cloud Error. Saved Locally.");
                                }
                            });
                        }
                    });
                }
            }
        });

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pop();
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        if (previousScreen != null) {
            previousScreen.render(shapeRenderer, spriteBatch);
        } else {
            ScreenUtils.clear(0, 0, 0, 1f);
        }

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
        bgTexture.dispose();

        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void show() {}
    @Override
    public void render(float delta) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
