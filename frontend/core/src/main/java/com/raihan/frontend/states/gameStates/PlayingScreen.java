package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.raihan.frontend.commands.*;
import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.entities.item.Items;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.entities.item.Spear;
import com.raihan.frontend.factories.BulletFactory;
import com.raihan.frontend.pools.Bullets;

import java.util.ArrayList;
import java.util.List;

public class PlayingScreen implements GameScreen{
    private GameStateManager gsm;
    private final List<Player> players; // Player 1 always the one controlled
    private final List<Enemies> enemies;
    private final List<Items> items;
    private final List<Command> commands;
    private final AttackCommand attackCommand;
    int Hour = 21, Minute = 0;
    private float timeTimer = 0f;
    private final float SECONDS_PER_MINUTE = 1.0f;
    private OrthographicCamera camera;
    private final BulletFactory bulletFactory;

    public PlayingScreen(GameStateManager gsm){
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        items = new ArrayList<>();
        commands = new ArrayList<>();
        this.gsm = gsm;
        bulletFactory = new BulletFactory();

        camera = new OrthographicCamera();

        float zoomLevel = 3f;
        camera.setToOrtho(false, Gdx.graphics.getWidth() / zoomLevel, Gdx.graphics.getHeight() / zoomLevel);

        players.add(new Player("placeholder", new Spear(), 0f, 0f));
        commands.add(new UpCommand(players.get(0)));
        commands.add(new RightCommand(players.get(0)));
        commands.add(new LeftCommand(players.get(0)));
        commands.add(new DownCommand(players.get(0)));
        commands.add(new RunCommand(players.get(0)));
        attackCommand = new AttackCommand(players.get(0));
    }

    @Override
    public void update(float delta) {
        timeTimer += delta;
        if (timeTimer >= SECONDS_PER_MINUTE) {
            Minute++;
            timeTimer = 0f;

            if (Minute >= 60) {
                Hour++;
                Minute = 0;
                if (Hour >= 24) Hour = 0;
            }
        }

        players.get(0).idle();
        if(!(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))) players.get(0).stopRun();
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) commands.get(4).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) commands.get(0).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) commands.get(1).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) commands.get(2).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) commands.get(3).execute();
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) attackCommand.execute(bulletFactory, enemies);


        for (Player p: players){
            p.update(delta);
        }
        for (Items i: items){
            i.update(delta);
        }

        Player main = players.get(0);
        camera.position.set(main.getPosition().x, main.getPosition().y, 0);
        camera.update();

        for (int i = bulletFactory.getInUse().size() - 1; i >= 0; i--) {
            Bullets b = bulletFactory.getInUse().get(i);
            b.update(delta);


            if (!b.isActive()) {
                bulletFactory.release(b);
            }
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.circle(0f, 0f, 50f);
        for (Player p: players){
            p.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
