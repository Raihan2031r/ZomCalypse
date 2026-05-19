package com.raihan.frontend.services;
import okhttp3.*;

import java.io.IOException;

public class BackendService {
    private static final boolean IS_DEV = false;
    private static final String DEV_URL = "http://localhost:8081";
    private static final String PROD_URL = "https://zomcalypse-backend.up.railway.app";
    private final String BASE_URL;
    private static final OkHttpClient client = new OkHttpClient();
    public BackendService() {
        BASE_URL = IS_DEV ? DEV_URL : PROD_URL;
    }

    public interface RequestCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void createPlayer(String username, String password, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(BASE_URL + "/auth/signup")
            .post(body)
            .build();

        send(request, callback);
    }

    public void loginPlayer(String username, String password, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json")
        );
        Request request = new Request.Builder()
            .url(BASE_URL + "/auth/login")
            .post(body)
            .build();

        send(request, callback);
    }

    public void getPlayer(String token, RequestCallback callback) {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/players/me")
            .get()
            .addHeader("Authorization", "Bearer " + token)
            .build();

        send(request, callback);
    }

    public void getLeaderboard(int limit, RequestCallback callback) {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/players/leaderboard/high-score?limit=" + limit)
            .get()
            .build();

        send(request, callback);
    }

    public void submitScore(String token, int score, int zombiesKilled, int daysPassed, RequestCallback callback) {
        String json = String.format(
            "{" +
                "\"value\":%d," +
                "\"zombies_killed\":%d," +
                "\"days_Passed\":%d" +
                "}",
            score, zombiesKilled, daysPassed
        );

        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(BASE_URL + "/api/scores")
            .post(body)
            .addHeader("Authorization", "Bearer " + token)
            .build();

        send(request, callback);
    }

    public void getPlayerSaves(String token, RequestCallback callback) {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/games")
            .get()
            .addHeader("Authorization", "Bearer " + token)
            .build();

        send(request, callback);
    }

    public void saveGameData(String token, String jsonSaveData, RequestCallback callback) {
        RequestBody body = RequestBody.create(
            jsonSaveData, MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(BASE_URL + "/api/games/save")
            .post(body)
            .addHeader("Authorization", "Bearer " + token)
            .build();

        send(request, callback);
    }

    public void pingBackend() {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/info")
            .get()
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.close();
            }
        });
    }

    private void send(Request request, RequestCallback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful() && responseBody != null) {
                        callback.onSuccess(responseBody.string());
                    } else {
                        callback.onError("HTTP " + response.code());
                    }
                }
            }
        });
    }
}
