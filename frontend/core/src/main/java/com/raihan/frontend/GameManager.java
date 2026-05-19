package com.raihan.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.raihan.frontend.observers.ScoreManager;
import com.raihan.frontend.services.BackendService;

public class GameManager {
    private static GameManager instance;
    private final ScoreManager scoreManager;
    private String authToken;
    private BackendService backendService;
    private String username;

    private GameManager(){
        scoreManager = new ScoreManager();
        backendService = new BackendService();
        backendService.pingBackend();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
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

    public String getUsername() {
        return username;
    }

    public void endGame() {
        if (authToken == null) {
            Gdx.app.log("SCORE", "Pemain offline, skor tidak dikirim ke server.");
            return;
        }

        int score = scoreManager.getTotalScore();
        int zombieKilled = scoreManager.getZombieKills();
        int daysPassed = scoreManager.getDaysSurvived();

        backendService.submitScore(authToken, score, zombieKilled, daysPassed, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.log("SCORE_SUCCESS", "Berhasil mensubmit score ke Leaderboard!");
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("SCORE_ERROR", error);
            }
        });
    }

    public String getAuthToken() {
        return authToken;
    }
}
