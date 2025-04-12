package com.battleshipclient;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;

public class Main extends GameApplication {

    private SceneManager sceneManager;

    @Override
    protected void initSettings(@NotNull GameSettings settings) {
        settings.setTitle(I18nLoader.getText("appTitle"));
        settings.setWidth(1440);
        settings.setHeight(810);
    }

    @Override
    protected void initGame() {
        Image mouseCursorImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/Mouse_Cursor.png")).toExternalForm());
        getGameScene().setCursor(new ImageCursor(mouseCursorImage));
        sceneManager = new SceneManager();
        sceneManager.showHomeScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}