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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raihan.frontend.services.BackendService;
import com.raihan.frontend.GameManager;

public class MenuScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;

    private Label leaderboardLabel;
    private Label userGreetingLabel;
    private TextButton profileBtn;
    private TextButton loginBtn;
    private Texture background;

    public MenuScreen(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage(new ScreenViewport());

        background = new Texture(Gdx.files.internal("menu_bg.png"));
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.WHITE;

        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();

        skin.add("default-font", customFont);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.15f, 0.25f, 0.50f, 1f);
        pixmap.fill();
        skin.add("btn-up", new Texture(pixmap));

        pixmap.setColor(0.25f, 0.4f, 0.7f, 1f);
        pixmap.fill();
        skin.add("btn-down", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.getDrawable("btn-up");
        btnStyle.down = skin.getDrawable("btn-down");
        btnStyle.font = skin.getFont("default-font");
        skin.add("default", btnStyle);

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top();
        stage.addActor(topTable);

        Label title = new Label("ZomCalypse", skin);
        title.setColor(Color.RED);
        title.setFontScale(2.5f);
        topTable.add(title).padTop(50).row();

        userGreetingLabel = new Label("Not Logged In", skin);
        userGreetingLabel.setAlignment(Align.center);
        userGreetingLabel.setColor(Color.YELLOW);
        topTable.add(userGreetingLabel).padTop(10).row();


        Table leftTable = new Table();
        leftTable.setFillParent(true);
        leftTable.left();
        stage.addActor(leftTable);

        TextButton playBtn = new TextButton("Play New Game", skin);
        TextButton loadBtn = new TextButton("Load Game", skin);
        TextButton settingBtn = new TextButton("Setting", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

        float btnWidth = 220f;
        float btnHeight = 50f;
        float padding = 10f;
        float leftPadding = 50f;

        leftTable.add(playBtn).size(btnWidth, btnHeight).pad(padding).padLeft(leftPadding).row();
        leftTable.add(loadBtn).size(btnWidth, btnHeight).pad(padding).padLeft(leftPadding).row();
        leftTable.add(settingBtn).size(btnWidth, btnHeight).pad(padding).padLeft(leftPadding).row();
        leftTable.add(exitBtn).size(btnWidth, btnHeight).pad(padding).padLeft(leftPadding).row();

        Table rightTable = new Table();
        rightTable.setFillParent(true);
        rightTable.right().bottom();
        stage.addActor(rightTable);

        float rightPadding = 50f;

        leaderboardLabel = new Label("Loading Leaderboard...", skin);
        leaderboardLabel.setAlignment(Align.left);
        rightTable.add(leaderboardLabel).padBottom(20).padRight(rightPadding).row();

        loginBtn = new TextButton("Login", skin);
        rightTable.add(loginBtn).size(200f, 40f).padBottom(10).padRight(rightPadding).row();

        profileBtn = new TextButton("My Profile", skin);
        profileBtn.setVisible(false);
        rightTable.add(profileBtn).size(200f, 40f).padBottom(40).padRight(rightPadding).row();

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new GamePreferenceScreen(gsm));
            }
        });

        loadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.push(new SaveScreen(gsm));
            }
        });

        settingBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new SettingScreen(gsm));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.push(new LoginScreen(gsm));
            }
        });

        profileBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new ProfileScreen(gsm));
            }
        });

        fetchLeaderboard();
        checkUserLoginStatus();

        Gdx.input.setInputProcessor(stage);
    }

    private void checkUserLoginStatus() {
        String token = GameManager.getInstance().getAuthToken();

        if (token != null && !token.isEmpty()) {
            BackendService backend = new BackendService();
            backend.getPlayer(token, new BackendService.RequestCallback() {
                @Override
                public void onSuccess(String response) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            JsonValue root = new JsonReader().parse(response);
                            String username = root.getString("username", "Survivor");
                            userGreetingLabel.setText("Welcome, " + username + "!");

                            profileBtn.setVisible(true);
                            loginBtn.setVisible(false);
                        } catch (Exception e) {
                            userGreetingLabel.setText("Welcome, Survivor!");
                            profileBtn.setVisible(true);
                            loginBtn.setVisible(false);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Gdx.app.postRunnable(() -> {
                        userGreetingLabel.setText("Offline Mode");
                    });
                }
            });
        }
    }

    private void fetchLeaderboard() {
        BackendService backend = new BackendService();
        backend.getLeaderboard(5, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.postRunnable(() -> {
                    try {
                        StringBuilder sb = new StringBuilder("--- GLOBAL LEADERBOARD ---\n");
                        JsonValue root = new JsonReader().parse(response);

                        int rank = 1;
                        for (JsonValue scoreNode : root) {
                            String username = scoreNode.has("player") ? scoreNode.get("player").getString("username", "Unknown") : "Unknown";
                            int score = scoreNode.getInt("value", 0);

                            sb.append(rank).append(". ").append(username).append(" - ").append(score).append(" pts\n");
                            rank++;
                        }

                        if(root.size == 0) sb.append("Belum ada skor yang tercatat.");
                        leaderboardLabel.setText(sb.toString());

                    } catch (Exception e) {
                        leaderboardLabel.setText("Format Leaderboard tidak sesuai.");
                    }
                });
            }

            @Override
            public void onError(String error) {
                Gdx.app.postRunnable(() -> {
                    leaderboardLabel.setText("Gagal memuat Leaderboard.\nCek koneksi internet.");
                });
            }
        });
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, stage.getWidth(), stage.getHeight());
        spriteBatch.end();

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
        background.dispose();

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
