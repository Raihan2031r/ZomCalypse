package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.item.Consumables;
import com.raihan.frontend.entities.item.Items;
import com.raihan.frontend.entities.item.Weapon;

public class InventoryScreen implements GameScreen {
    private final GameStateManager gsm;
    private final GameScreen previousScreen;
    private final Player player;

    private final Stage stage;
    private final Skin skin;
    private final Table mainTable;
    private final Texture slotTexture;
    private final Texture bgTexture;

    public InventoryScreen(GameStateManager gsm, GameScreen previousScreen, Player player) {
        this.gsm = gsm;
        this.previousScreen = previousScreen;
        this.player = player;
        this.stage = new Stage(new ScreenViewport());

        skin = new Skin();

        BitmapFont defaultFont = new BitmapFont();

        skin.add("default-font", defaultFont);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.3f, 0.3f, 1f);
        pixmap.fill();
        slotTexture = new Texture(pixmap);

        pixmap.setColor(0.1f, 0.1f, 0.1f, 0.95f);
        pixmap.fill();
        bgTexture = new Texture(pixmap);
        pixmap.dispose();

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        refreshInventory();

        Gdx.input.setInputProcessor(stage);
    }

    private void refreshInventory() {
        mainTable.clear();

        Table window = new Table();
        window.setBackground(new Image(bgTexture).getDrawable());
        window.pad(20);

        Label title = new Label("INVENTORY", skin);
        window.add(title).colspan(5).center().padBottom(20).row();

        int cols = 5;
        int currentItem = 0;

        for (Items item : player.getItems()) {
            Table slot = new Table();
            slot.setBackground(new Image(slotTexture).getDrawable());

            Image icon = new Image(slotTexture);
            icon.setColor(getColorByItemType(item));
            slot.add(icon).size(50, 50).pad(5).row();

            Label itemName = new Label(item.getName(), skin);
            itemName.setFontScale(0.8f);
            slot.add(itemName).padBottom(5);

            slot.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    useItem(item);
                }
            });

            window.add(slot).size(80, 110).pad(5);
            currentItem++;
            if (currentItem % cols == 0) window.row();
        }

        mainTable.add(window).center();
    }

    private void useItem(Items item) {
        if (item instanceof Consumables) {
            ((Consumables) item).use();
            player.drop(item);
            refreshInventory();
        } else if (item instanceof Weapon) {
            player.changeWeapon((Weapon) item);
        }
    }

    private Color getColorByItemType(Items item) {
        if (item.getName().equals("Food")) return Color.GREEN;
        if (item.getName().equals("Drink")) return Color.CYAN;
        if (item.getName().equals("Ammo")) return Color.YELLOW;
        if (item.getName().equals("Bandage")) return Color.RED;
        if (item instanceof Weapon) return Color.GRAY;
        return Color.WHITE;
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pop();
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        if (previousScreen != null) {
            previousScreen.render(shapeRenderer, spriteBatch);
        } else {
            ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
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
        slotTexture.dispose();
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
