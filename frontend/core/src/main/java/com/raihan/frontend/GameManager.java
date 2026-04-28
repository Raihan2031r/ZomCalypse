package com.raihan.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.raihan.frontend.services.BackendService;

public class GameManager {
    private static GameManager instance;
    private String authToken;
    private BackendService backendService;
    private String username;
    private String currentPlayerId = null;
    private int score = 0;
    private int zombieKilled = 0;
    private int nigthsPassed = 0;
    private boolean gameActive;

    private GameManager(){
        backendService = new BackendService();
        gameActive = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public void setAuthToken(String token) {
        authToken = token;
    }

    public interface LoginCallback {
        void onSuccess(String token);
        void onError(String error);
    }

    public void loginPlayer(String username, String password, LoginCallback callback) {
        backendService.loginPlayer(username, password, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JsonValue parse = new JsonReader().parse(response);
                    authToken = parse.getString("token");
                    Gdx.app.log("PLAYER", "Login berhasil, token: " + authToken);
                    callback.onSuccess(authToken);
                } catch (Exception e) {
                    Gdx.app.error("JSON_ERROR", "Gagal parsing token", e);
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("LOGIN_ERROR", error);
                callback.onError(error);
            }
        });
    }

    public void registerPlayer(String username, String password, LoginCallback callback) {
        backendService.createPlayer(username, password, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.log("PLAYER", "Pendaftaran berhasil");
                loginPlayer(username, password, callback);
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("REGISTER_ERROR", error);
                callback.onError(error);
            }
        });
    }
}
