package com.battleshipclient;

import com.battleshipclient.enums.HitType;
import com.battleshipclient.records.JoinGameResponse;
import com.battleshipclient.records.Ship;
import com.battleshipclient.status.GameStatus;
import com.battleshipclient.status.UserStatus;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiService {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String baseUrl = "https://api.bs.sh00ckbass.de";
    private static final Gson gson = new Gson();

    public static void registerUser(String username, String password) {
        String body = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            loginUser(username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void loginUser(String username, String password) {
        String body = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            UserStatus.setAccessToken(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createGame() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/game/create"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + UserStatus.getAccessToken())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            GameStatus.setGameKey(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void joinGame(String gameKey) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/game/join"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + UserStatus.getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(gameKey))
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JoinGameResponse joinResponse = gson.fromJson(response.body(), JoinGameResponse.class);
            GameStatus.setOpponentUserName(joinResponse.challengerName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setupBoard(List<Ship> ships) {
        JsonArray shipArray = new JsonArray();
        for (Ship ship : ships) {
            shipArray.add(gson.toJsonTree(ship));
        }

        JsonObject result = new JsonObject();
        result.add("ships", shipArray);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/game/setup-board"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + UserStatus.getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(result)))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void surrender() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/game/surrender"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + UserStatus.getAccessToken())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void hit(int posX, int posY) {
        String body = String.format("""
                {
                    "x": %d,
                    "y": %d
                }
                """, posX, posY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/game/hit"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + UserStatus.getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            GameStatus.setCurrentHitType(HitType.valueOf(response.body()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
