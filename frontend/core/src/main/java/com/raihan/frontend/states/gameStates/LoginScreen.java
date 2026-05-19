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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.raihan.frontend.GameManager;

public class LoginScreen implements GameScreen {
    private final GameStateManager gsm;
    private final Stage stage;
    private final Skin skin;

    private TextField usernameField;
    private TextField passwordField;
    private Label statusLabel;

    public LoginScreen(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage(new FitViewport(1280, 720));
        this.skin = new Skin();

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new Image(createColorPixmap(0.1f, 0.1f, 0.15f, 1f)).getDrawable());
        stage.addActor(root);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.WHITE;

        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();

        skin.add("default-font", customFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE);
        skin.add("default", labelStyle);

        Pixmap btnUpPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        btnUpPixmap.setColor(0.3f, 0.3f, 0.3f, 1f);
        btnUpPixmap.fill();
        skin.add("btn-up", new Texture(btnUpPixmap));

        Pixmap btnDownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        btnDownPixmap.setColor(0.5f, 0.5f, 0.5f, 1f);
        btnDownPixmap.fill();
        skin.add("btn-down", new Texture(btnDownPixmap));

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.getDrawable("btn-up");
        btnStyle.down = skin.getDrawable("btn-down");
        btnStyle.font = skin.getFont("default-font");
        skin.add("default", btnStyle);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = skin.getFont("default-font");
        tfStyle.fontColor = Color.WHITE;

        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0.2f, 0.2f, 0.2f, 1f);
        bgPixmap.fill();
        tfStyle.background = new TextureRegionDrawable(new Texture(bgPixmap));

        Pixmap cursorPixmap = new Pixmap(2, (int)customFont.getLineHeight(), Pixmap.Format.RGBA8888);
        cursorPixmap.setColor(Color.WHITE);
        cursorPixmap.fill();
        tfStyle.cursor = new TextureRegionDrawable(new Texture(cursorPixmap));

        Pixmap selectionPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        selectionPixmap.setColor(0.3f, 0.3f, 0.8f, 0.5f);
        selectionPixmap.fill();
        tfStyle.selection = new TextureRegionDrawable(new Texture(selectionPixmap));

        skin.add("default", tfStyle);

        btnUpPixmap.dispose();
        btnDownPixmap.dispose();
        bgPixmap.dispose();
        cursorPixmap.dispose();
        selectionPixmap.dispose();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("LOGIN", skin);
        title.setFontScale(2f);
        table.add(title).colspan(2).padBottom(30).row();

        table.add(new Label("Username:", skin)).right().padRight(10);
        usernameField = new TextField("", skin);
        table.add(usernameField).width(250).height(50).padBottom(15).row();

        table.add(new Label("Password:", skin)).right().padRight(10);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(250).height(50).padBottom(20).row();

        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.YELLOW);
        table.add(statusLabel).colspan(2).padBottom(20).row();

        Table btnTable = new Table();
        TextButton loginBtn = new TextButton("Login", skin);
        TextButton registerBtn = new TextButton("Register", skin);
        TextButton backBtn = new TextButton("Back", skin);

        btnTable.add(loginBtn).size(120, 50).padRight(10);
        btnTable.add(registerBtn).size(120, 50);
        table.add(btnTable).colspan(2).padBottom(20).row();
        table.add(backBtn).size(250, 50).colspan(2).row();

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String user = usernameField.getText();
                String pass = passwordField.getText();

                if(user.isEmpty() || pass.isEmpty()) {
                    statusLabel.setText("All fields must be filled!");
                    return;
                }

                statusLabel.setText("Checking Credential...");
                GameManager.getInstance().loginPlayer(user, pass, new GameManager.LoginCallback() {
                    @Override
                    public void onSuccess(String token) {
                        Gdx.app.postRunnable(() -> {
                            System.out.println("Login Success!");
                            gsm.set(new MenuScreen(gsm));
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Gdx.app.postRunnable(() -> statusLabel.setText("Login Error: " + error));
                    }
                });
            }
        });

        registerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String user = usernameField.getText();
                String pass = passwordField.getText();

                if(user.isEmpty() || pass.isEmpty()) {
                    statusLabel.setText("All fields must be filled!");
                    return;
                }

                statusLabel.setText("Registering Account...");
                GameManager.getInstance().registerPlayer(user, pass, new GameManager.LoginCallback() {
                    @Override
                    public void onSuccess(String token) {
                        Gdx.app.postRunnable(() -> {
                            System.out.println("Register Success!");
                            gsm.set(new MenuScreen(gsm));
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Gdx.app.postRunnable(() -> statusLabel.setText("Register Error: " + error));
                    }
                });
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

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override public void update(float delta) {}

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        if (Gdx.input.getInputProcessor() == stage) Gdx.input.setInputProcessor(null);
    }

    private Texture createColorPixmap(float r, float g, float b, float a) {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(r, g, b, a);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
