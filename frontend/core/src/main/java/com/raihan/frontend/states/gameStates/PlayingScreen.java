package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.raihan.frontend.GameManager;
import com.raihan.frontend.MapManager;
import com.raihan.frontend.commands.*;
import com.raihan.frontend.entities.enemies.Enemies;
import com.raihan.frontend.entities.enemies.Zombies;
import com.raihan.frontend.entities.interactables.Crates;
import com.raihan.frontend.entities.item.*;
import com.raihan.frontend.entities.Player;
import com.raihan.frontend.factories.BulletFactory;
import com.raihan.frontend.factories.ZombieFactory;
import com.raihan.frontend.observers.PlayerUIObserver;
import com.raihan.frontend.observers.ScoreUIObserver;
import com.raihan.frontend.pools.Bullets;
import com.raihan.frontend.strategies.DifficultyStrategy;

import java.util.ArrayList;
import java.util.List;

public class PlayingScreen implements GameScreen{
    private GameStateManager gsm;
    private final GameManager gm;
    private final List<Player> players; // Player 1 always the one controlled
    private final List<Items> items;
    private final List<Command> commands;
    private final AttackCommand attackCommand;
    int Hour = 21, Minute = 0;
    private float timeTimer = 0f;
    private final float SECONDS_PER_MINUTE = 1.0f;
    private OrthographicCamera camera;
    private final BulletFactory bulletFactory;
    private ZombieFactory zombieFactory;
    private DifficultyStrategy difficultyStrategy;
    private float waveTimer = 0f;

    private final MapManager mapManager;
    private PlayerUIObserver playerUI;
    private ScoreUIObserver scoreUI;

    float gamePauseCooldown = 0f;
    float deathTimer = 0f;
    float zoomLevel = 2.5f;

    public PlayingScreen(GameStateManager gsm, DifficultyStrategy difficulty){
        players = new ArrayList<>();
        items = new ArrayList<>();
        commands = new ArrayList<>();
        this.gsm = gsm;
        bulletFactory = new BulletFactory();
        zombieFactory = new ZombieFactory();
        difficultyStrategy = difficulty;
        Gdx.input.setCursorCatched(true);

        camera = new OrthographicCamera();
        mapManager = new MapManager("maps/untitled.tmx");
        gm = GameManager.getInstance();

        camera.setToOrtho(false, Gdx.graphics.getWidth() / zoomLevel, Gdx.graphics.getHeight() / zoomLevel);

        String name = gm.getUsername();
        if(name == null)  name = "You";
        players.add(new Player(name, new Spear(), mapManager.getPlayerSpawnArea().x, mapManager.getPlayerSpawnArea().y));
        players.get(0).pickUp(new Spear());
        playerUI = new PlayerUIObserver();
        scoreUI = new ScoreUIObserver();
        players.get(0).addObserver(playerUI);
        commands.add(new UpCommand(players.get(0)));
        commands.add(new RightCommand(players.get(0)));
        commands.add(new LeftCommand(players.get(0)));
        commands.add(new DownCommand(players.get(0)));
        commands.add(new RunCommand(players.get(0)));
        attackCommand = new AttackCommand(players.get(0));
    }

    @Override
    public void update(float delta) {
        if(gamePauseCooldown > 0f) gamePauseCooldown -= delta;
        timeTimer += delta;
        waveTimer += delta;

        if (players.get(0).getHP() <= 0) {
            deathTimer += delta;

            if (deathTimer > 2.0f) {
                Gdx.input.setCursorCatched(false);
                gsm.set(new GameOverScreen(gsm));
            }

            players.get(0).update(delta);
            return;
        }

        if (timeTimer >= SECONDS_PER_MINUTE) {
            Minute++;
            timeTimer = 0f;

            if (Minute >= 60) {
                Hour++;
                Minute = 0;
                if (Hour >= 24) {
                    Hour = 0;
                    gm.getScoreManager().addDay();
                }
            }
        }

        if (waveTimer >= difficultyStrategy.getTimePerWave()){
            waveTimer = 0f;

            zombieFactory.spawnWave(difficultyStrategy, mapManager.getZombieSpawnAreas());
        }

        players.get(0).idle();
        if(!(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))) players.get(0).stopRun();
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) commands.get(4).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) commands.get(0).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) commands.get(1).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) commands.get(2).execute();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) commands.get(3).execute();
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            for (Items i: players.get(0).getItems()) {
                if (players.get(0).getWeapon() instanceof Rifle){
                    if (i instanceof Spear) {
                        players.get(0).changeWeapon((Weapon) i);
                        break;
                    }
                } else if (players.get(0).getWeapon() instanceof Spear){
                    if (i instanceof Rifle) {
                        players.get(0).changeWeapon((Weapon) i);
                        break;
                    }
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && players.get(0).getWeapon() instanceof Rifle) {
            List<Enemies> activeEnemies  = new ArrayList<>(zombieFactory.getInUse());
            attackCommand.execute(bulletFactory, activeEnemies);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && players.get(0).getWeapon() instanceof Spear) {
            List<Enemies> activeEnemies  = new ArrayList<>(zombieFactory.getInUse());
            attackCommand.execute(bulletFactory, activeEnemies);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Player mainPlayer = players.get(0);
            Rectangle pRect = mainPlayer.getCollider();

            Rectangle interactRange = new Rectangle(
                pRect.x - 10, pRect.y - 10,
                pRect.width + 20, pRect.height + 20
            );

            for (Crates c: mapManager.getInteractables()){
                if (c.getBounds().overlaps(interactRange)){
                    c.interact(mainPlayer, gm.getScoreManager());
                    break;
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gamePauseCooldown <= 0f) {
            gamePauseCooldown = 1f;
            pause();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R) && players.get(0).getWeapon() instanceof Rifle){
            ((Rifle) players.get(0).getWeapon()).Reload();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.B) && gamePauseCooldown <= 0f){
            gamePauseCooldown = 1f;
            Gdx.input.setCursorCatched(false);
            gsm.push(new InventoryScreen(gsm, this, players.get(0)));
        }

        for (Player p: players){
            p.update(delta);
            if(p.getAttackDelay() > 0f && p.getWeapon() instanceof Spear){
                p.stopMoving();
            } else p.moveAndCollide(delta, mapManager.getSolidObstacles());
        }

        for (int i = zombieFactory.getInUse().size() - 1; i >= 0; i--) {
            Zombies z = zombieFactory.getInUse().get(i);
                for(Player p: players){
                    z.update(delta, players.get(0));
                }
            z.moveAndCollide(delta, mapManager.getSolidObstacles());
            //System.out.println(z.getPosition());

            if (z.getHP() <= 0) {
                zombieFactory.release(z);
                gm.getScoreManager().addKill();
            }
        }

        for (Items i: items){
            i.update(delta);
        }

        for (Crates c : mapManager.getInteractables()) {
            c.update(delta);
        }

        Player main = players.get(0);
        camera.position.set(main.getPosition().x, main.getPosition().y, 0);
        camera.update();

        for (int i = bulletFactory.getInUse().size() - 1; i >= 0; i--) {
            Bullets b = bulletFactory.getInUse().get(i);
            b.update(delta);

            if (b.isActive()) {
                for (Zombies z : zombieFactory.getInUse()) {
                    if (b.getCollider().overlaps(z.getCollider())) {
                        z.takeDamage(b.getDamage());
                        b.hit();
                        break;
                    }
                }
            }

            if (b.isActive()) {
                for (Rectangle wall : mapManager.getSolidObstacles()) {
                    if (b.getCollider().overlaps(wall)) {
                        b.hit();
                        break;
                    }
                }
            }

            if (!b.isActive()) {
                bulletFactory.release(b);
            }
        }

        scoreUI.update(gm.getScoreManager().getTotalScore());
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        mapManager.getMapRenderer().setView(camera);

        mapManager.getMapRenderer().getBatch().begin();
        for (TiledMapTileLayer layer : mapManager.getBackgroundLayers()) {
            mapManager.getMapRenderer().renderTileLayer(layer);
        }
        mapManager.getMapRenderer().getBatch().end();


        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Player p: players){
            p.renderTexture(spriteBatch);
        }
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Zombies z: zombieFactory.getInUse()){
            z.render(shapeRenderer);
        }
        bulletFactory.render(shapeRenderer);
        shapeRenderer.end();


        boolean isHidden = false;
        Player mainPlayer = players.get(0);
        for (Rectangle roof : mapManager.getRoofAreas()) {
            if (mainPlayer.getCollider().overlaps(roof)) {
                isHidden = true;
                break;
            }
        }

        float alpha = isHidden ? 0.4f : 1.0f;
        mapManager.getMapRenderer().getBatch().begin();
        mapManager.getMapRenderer().getBatch().setColor(1f, 1f, 1f, alpha);

        for (TiledMapTileLayer layer : mapManager.getForegroundLayers()) {
            mapManager.getMapRenderer().renderTileLayer(layer);
        }

        mapManager.getMapRenderer().getBatch().setColor(1f, 1f, 1f, 1f);
        mapManager.getMapRenderer().getBatch().end();

        float timeRemaining = difficultyStrategy.getTimePerWave() - waveTimer;
        if (timeRemaining < 0) timeRemaining = 0;

        playerUI.render(Hour, Minute, timeRemaining);
        scoreUI.render();
    }

    @Override
    public void dispose() {
        for (Player p: players){
            p.dispose();
            playerUI.dispose();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / zoomLevel;
        camera.viewportHeight = height / zoomLevel;
        camera.update();
        if (playerUI != null) playerUI.resize(width, height);
    }

    @Override
    public void pause() {
        Gdx.input.setCursorCatched(false);
        gsm.push(new PauseScreen(gsm, this));
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
