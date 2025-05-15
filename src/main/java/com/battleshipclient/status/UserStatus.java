package com.battleshipclient.status;

public class UserStatus {

    private static boolean isInGame = false;
    private static String username;
    private static boolean isLoggedIn = false;
    private static String accessToken;

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setInGameStatus(boolean inGame) {
        isInGame = inGame;
    }

    public static boolean getInGameStatus() {
        return isInGame;
    }

    public static void setUsername(String uname) {
        username = uname;
    }

    public static String getUsername() {
        return username;
    }

    public static void setIsLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public static boolean getIsLoggedIn() {
        return isLoggedIn;
    }
}
