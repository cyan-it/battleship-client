package com.battleshipclient;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class Main extends GameApplication {

    private SceneManager sceneManager;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Battleship");
        settings.setWidth(800);
        settings.setHeight(600);
    }

    @Override
    protected void initGame() {
        sceneManager = new SceneManager();
        sceneManager.showHomeScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}