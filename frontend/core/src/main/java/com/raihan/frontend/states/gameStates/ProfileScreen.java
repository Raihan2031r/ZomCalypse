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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raihan.frontend.GameManager;
import com.raihan.frontend.services.BackendService;

public class ProfileScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;

    private Label usernameLabel;
    private Label joinDateLabel;
    private Label playerIdLabel;
    private Label statusLabel;

    public ProfileScreen(GameStateManager gsm) {
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
        pixmap.setColor(0.3f, 0.3f, 0.3f, 1f);
        pixmap.fill();
        skin.add("btn-up", new Texture(pixmap));
        pixmap.setColor(0.5f, 0.5f, 0.5f, 1f);
        pixmap.fill();
        skin.add("btn-down", new Texture(pixmap));
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
        stage.addActor(table);

        Label title = new Label("PLAYER PROFILE", skin);
        title.setFontScale(2f);
        table.add(title).padBottom(30).row();

        usernameLabel = new Label("Username: Loading...", skin);
        playerIdLabel = new Label("Player ID: Loading...", skin);
        joinDateLabel = new Label("Joined At: Loading...", skin);
        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.YELLOW);

        table.add(usernameLabel).padBottom(15).row();
        table.add(playerIdLabel).padBottom(15).row();
        table.add(joinDateLabel).padBottom(30).row();
        table.add(statusLabel).padBottom(20).row();

        TextButton backBtn = new TextButton("Back to Menu", skin);
        table.add(backBtn).size(200f, 50f).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new MenuScreen(gsm));
            }
        });

        fetchPlayerData();

        Gdx.input.setInputProcessor(stage);
    }

    private void fetchPlayerData() {
        String token = GameManager.getInstance().getAuthToken();

        if (token == null || token.isEmpty()) {
            statusLabel.setText("Error: You are not logged in!");
            statusLabel.setColor(Color.RED);
            return;
        }

        BackendService backend = new BackendService();
        backend.getPlayer(token, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.postRunnable(() -> {
                    try {
                        JsonValue root = new JsonReader().parse(response);

                        String username = root.getString("username", "Unknown");
                        String playerId = root.getString("playerId", "Unknown");
                        String rawDate = root.getString("createdAt", "Unknown");

                        String cleanDate = rawDate;
                        if (rawDate.contains("T")) {
                            cleanDate = rawDate.split("T")[0];
                        }

                        usernameLabel.setText("Username: " + username);
                        playerIdLabel.setText("Player ID: " + playerId);
                        joinDateLabel.setText("Joined At: " + cleanDate);
                        statusLabel.setText("Data loaded successfully!");
                        statusLabel.setColor(Color.GREEN);

                    } catch (Exception e) {
                        statusLabel.setText("Failed to parse profile data.");
                        statusLabel.setColor(Color.RED);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Error fetching profile: " + error);
                    statusLabel.setColor(Color.RED);
                });
            }
        });
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
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
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
