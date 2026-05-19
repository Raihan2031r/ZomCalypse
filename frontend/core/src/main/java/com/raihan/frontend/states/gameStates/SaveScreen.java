package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raihan.frontend.GameManager;
import com.raihan.frontend.save.SaveDTO;
import com.raihan.frontend.save.SaveManager;
import com.raihan.frontend.services.BackendService;

public class SaveScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;
    private final Table saveListTable;

    public SaveScreen(GameStateManager gsm) {
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

        Label titleLabel = new Label("SELECT GAME SAVES", skin);
        titleLabel.setFontScale(1.5f);
        root.add(titleLabel).pad(20).row();

        saveListTable = new Table();
        ScrollPane scrollPane = new ScrollPane(saveListTable);
        root.add(scrollPane).expand().fill().pad(20).row();

        TextButton fetchDbBtn = createButton("Fetch data from Cloud");
        root.add(fetchDbBtn).size(300, 50).pad(10).row();

        TextButton backBtn = createButton("Back to Menu");
        root.add(backBtn).size(200, 50).padBottom(20).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuScreen(gsm));
            }
        });

        fetchDbBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fetchSavesFromDatabase();
            }
        });

        loadLocalSaves();

        Gdx.input.setInputProcessor(stage);
    }

    private void loadLocalSaves() {
        saveListTable.clear();

        FileHandle[] saveFiles = SaveManager.getAllLocalSaves();

        if (saveFiles.length > 0) {
            for (FileHandle file : saveFiles) {
                SaveDTO localSave = SaveManager.load(file.name());

                if (localSave != null) {
                    String info = "Lokal: " + file.name() + " | Game Time: " + localSave.gameHour + ":" + localSave.gameMinute;
                    addSaveSlot("LOKAL", localSave, info);
                }
            }
        } else {
            saveListTable.add(new Label("No Saves found in this device", skin)).pad(10).row();
        }
    }

    private void fetchSavesFromDatabase() {
        String token = GameManager.getInstance().getAuthToken();
        if (token == null) {
            saveListTable.add(new Label("Please Login to save to cloud!", skin)).row();
            return;
        }

        saveListTable.add(new Label("Fetching data from server...", skin)).row();

        new BackendService().getPlayerSaves(token, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.postRunnable(() -> {
                    saveListTable.clear();

                    FileHandle[] saveFiles = SaveManager.getAllLocalSaves();
                    if (saveFiles.length > 0) {
                        for (FileHandle file : saveFiles) {
                            SaveDTO localSave = SaveManager.load(file.name());
                            if (localSave != null) {
                                String info = "Lokal: " + file.name() + " | Game Time: " + localSave.gameHour + ":" + localSave.gameMinute;
                                addSaveSlot("LOKAL", localSave, info);
                            }
                        }
                    }

                    try {
                        JsonValue root = new JsonReader().parse(response);
                        Json jsonLibgdx = new Json();

                        for (JsonValue saveJson : root) {
                            SaveDTO cloudSave = jsonLibgdx.fromJson(SaveDTO.class, saveJson.toString());
                            String dbDate = saveJson.has("created_at") ? saveJson.getString("created_at") : "Cloud Save";

                            addSaveSlot("CLOUD", cloudSave, "Saved in Cloud| " + dbDate);
                        }

                        if (root.size == 0 && saveFiles.length == 0) {
                            saveListTable.add(new Label("No saves found.", skin)).pad(10).row();
                        }
                    } catch (Exception e) {
                        saveListTable.add(new Label("Failed to read the save data.", skin)).row();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Gdx.app.postRunnable(() -> {
                    saveListTable.add(new Label("Cannot communicate with server. Check your internet.", skin)).row();
                });
            }
        });
    }

    private void addSaveSlot(String tipe, SaveDTO saveData, String infoText) {
        Table slot = new Table();
        slot.setBackground(skin.getDrawable("bg-slot"));
        slot.pad(15);

        Color textColor = tipe.equals("CLOUD") ? Color.YELLOW : Color.GREEN;
        Label infoLabel = new Label("[" + tipe + "] " + infoText, skin);
        infoLabel.setColor(textColor);

        slot.add(infoLabel).expandX().left();

        TextButton loadBtn = createButton("LOAD GAME");
        slot.add(loadBtn).size(120, 40).right();

        loadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new PlayingScreen(gsm, saveData));
            }
        });

        saveListTable.add(slot).fillX().pad(5).row();
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

    @Override public void update(float delta) {}
    @Override public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0,0,0,1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
