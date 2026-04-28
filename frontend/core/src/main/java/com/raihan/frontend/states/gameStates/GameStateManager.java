package com.raihan.frontend.states.gameStates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

public class GameStateManager {
    private final Stack<GameScreen> states;

    public GameStateManager(){ states = new Stack<>(); }
    public void push(GameScreen screen){ states.push(screen); }
    public void pop(GameScreen screen){
        states.pop().dispose();
        push(screen);
    }

    public void update(float delta) {
        if (!states.isEmpty()) {
            states.peek().update(delta);
        }
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        if (!states.isEmpty()) {
            states.peek().render(shapeRenderer, batch);
        }
    }

    public void set(GameScreen state) {
        if (!states.isEmpty()) {
            states.pop().dispose();
        }
        states.push(state);
    }

    public void resize(int width, int height) {
        if (!states.isEmpty()) {
            states.peek().resize(width, height);
        }
    }

    public void clear() {
        while (!states.isEmpty()) {
            states.pop().dispose();
        }
    }

    public GameScreen getCurrentState(){
        return states.peek();
    }
}
