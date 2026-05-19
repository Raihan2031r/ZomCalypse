package com.raihan.frontend.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

public class BackendService {
    private static final boolean IS_DEV = false;
    private static final String DEV_URL = "http://localhost:8081";
    private static final String PROD_URL = "https://zomcalypse-backend.up.railway.app";
    private final String BASE_URL;

    public BackendService() {
        BASE_URL = IS_DEV ? DEV_URL : PROD_URL;
    }

    public interface RequestCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void createPlayer(String username, String password, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(BASE_URL + "/auth/signup");
        request.setHeader("Content-Type", "application/json");
        request.setContent(json);

        send(request, callback);
    }

    public void loginPlayer(String username, String password, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(BASE_URL + "/auth/login");
        request.setHeader("Content-Type", "application/json");
        request.setContent(json);

        send(request, callback);
    }

    public void getPlayer(String token, RequestCallback callback) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(BASE_URL + "/api/players/me");
        request.setHeader("Authorization", "Bearer " + token);

        send(request, callback);
    }

    public void getLeaderboard(int limit, RequestCallback callback) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(BASE_URL + "/api/players/leaderboard/high-score?limit=" + limit);

        send(request, callback);
    }

    public void submitScore(String token, int score, int zombiesKilled, int daysPassed, RequestCallback callback) {
        String json = "{" +
            "\"value\":" + score + "," +
            "\"zombies_killed\":" + zombiesKilled + "," +
            "\"days_Passed\":" + daysPassed +
            "}";

        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(BASE_URL + "/api/scores");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Bearer " + token);
        request.setContent(json);

        send(request, callback);
    }

    public void getPlayerSaves(String token, RequestCallback callback) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(BASE_URL + "/api/games");
        request.setHeader("Authorization", "Bearer " + token);

        send(request, callback);
    }

    public void saveGameData(String token, String jsonSaveData, RequestCallback callback) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(BASE_URL + "/api/games/save");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Bearer " + token);
        request.setContent(jsonSaveData);

        send(request, callback);
    }

    public void pingBackend() {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(BASE_URL + "/api/info");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
            }

            @Override
            public void failed(Throwable t) {
            }

            @Override
            public void cancelled() {
            }
        });
    }

    private void send(Net.HttpRequest request, final RequestCallback callback) {
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();

                if (statusCode >= 200 && statusCode < 300) {
                    Gdx.app.postRunnable(() -> callback.onSuccess(responseString));
                } else {
                    Gdx.app.postRunnable(() -> callback.onError("HTTP " + statusCode + " - " + responseString));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.postRunnable(() -> callback.onError(t.getMessage()));
            }

            @Override
            public void cancelled() {
                Gdx.app.postRunnable(() -> callback.onError("Request cancelled"));
            }
        });
    }
}
