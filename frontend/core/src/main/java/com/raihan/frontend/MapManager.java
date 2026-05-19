package com.raihan.frontend;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.raihan.frontend.entities.interactables.Boxes;
import com.raihan.frontend.entities.interactables.Crates;
import com.raihan.frontend.entities.interactables.Trash;
import com.raihan.frontend.entities.interactables.VendingMachine;
import com.raihan.frontend.entities.interactables.WeaponCrate;
import com.raihan.frontend.factories.ItemFactory;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final List<Rectangle> solidObstacles = new ArrayList<>();
    private final List<Crates> interactables = new ArrayList<>();
    private final List<Rectangle> zombieSpawnAreas = new ArrayList<>();
    private Rectangle playerSpawnArea;

    private final List<TiledMapTileLayer> backgroundLayers = new ArrayList<>();
    private final List<TiledMapTileLayer> foregroundLayers = new ArrayList<>();
    private final List<Rectangle> roofAreas = new ArrayList<>();

    private final float MAP_SCALE = 1f;
    private final ItemFactory itemFactory;

    public MapManager(String mapFilePath) {
        tiledMap = new TmxMapLoader().load(mapFilePath);
        this.itemFactory = new ItemFactory();
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);
        extractColliders();
    }

    private void extractColliders() {
        processLayers(tiledMap.getLayers());
    }

    private void processLayers(MapLayers layers) {
        for (MapLayer layer : layers) {
            if (layer instanceof TiledMapTileLayer) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                Object typeProp = tileLayer.getProperties().get("type");

                if (typeProp != null && typeProp.toString().equals("roof")) {
                    foregroundLayers.add(tileLayer);

                    for (int x = 0; x < tileLayer.getWidth(); x++) {
                        for (int y = 0; y < tileLayer.getHeight(); y++) {
                            if (tileLayer.getCell(x, y) != null) {
                                float rectX = x * tileLayer.getTileWidth() * MAP_SCALE;
                                float rectY = y * tileLayer.getTileHeight() * MAP_SCALE;
                                float rectW = tileLayer.getTileWidth() * MAP_SCALE;
                                float rectH = tileLayer.getTileHeight() * MAP_SCALE;
                                roofAreas.add(new Rectangle(rectX, rectY, rectW, rectH));
                            }
                        }
                    }
                } else {
                    backgroundLayers.add(tileLayer);
                }
            }

            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Object propertyType = object.getProperties().get("type");
                    if (propertyType != null) {
                        String typeName = propertyType.toString();
                        Rectangle rawRect = ((RectangleMapObject) object).getRectangle();
                        Rectangle scaledRect = new Rectangle(
                            rawRect.x * MAP_SCALE, rawRect.y * MAP_SCALE,
                            rawRect.width * MAP_SCALE, rawRect.height * MAP_SCALE
                        );

                        if (typeName.equals("boxes")) interactables.add(new Boxes(scaledRect, itemFactory));
                        else if (typeName.equals("trash")) interactables.add(new Trash(scaledRect, itemFactory));
                        else if (typeName.equals("vending machine")) interactables.add(new VendingMachine(scaledRect, itemFactory));
                        else if (typeName.equals("weapon crate")) interactables.add(new WeaponCrate(scaledRect, itemFactory));

                        if (typeName.equals("building") || typeName.equals("wall") ||
                            typeName.equals("fence") || typeName.equals("electric fence") ||
                            typeName.equals("boxes") || typeName.equals("trash") ||
                            typeName.equals("vending machine") || typeName.equals("weapon crate") ||
                            typeName.equals("boundary") || typeName.equals("street decoration")) {

                            solidObstacles.add(scaledRect);
                        }

                        if (typeName.equals("ZombieSpawner")) zombieSpawnAreas.add(scaledRect);
                        else if (typeName.equals("PlayerSpawner")) playerSpawnArea = scaledRect;
                    }
                }
            }

            if (layer instanceof MapGroupLayer) {
                processLayers(((MapGroupLayer) layer).getLayers());
            }
        }
    }

    public List<Crates> getInteractables() { return interactables; }
    public OrthogonalTiledMapRenderer getMapRenderer() { return mapRenderer; }
    public List<Rectangle> getSolidObstacles() { return solidObstacles; }
    public List<Rectangle> getZombieSpawnAreas() { return zombieSpawnAreas; }
    public Rectangle getPlayerSpawnArea() { return playerSpawnArea; }

    public List<TiledMapTileLayer> getBackgroundLayers() { return backgroundLayers; }
    public List<TiledMapTileLayer> getForegroundLayers() { return foregroundLayers; }
    public List<Rectangle> getRoofAreas() { return roofAreas; }

    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}
