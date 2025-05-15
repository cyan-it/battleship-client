package com.battleshipclient;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.battleshipclient.utils.I18nLoader;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Main extends GameApplication {

    @Override
    protected void initSettings(@NotNull GameSettings settings) {
        settings.setTitle(I18nLoader.getText("appTitle"));
        settings.setWidth(1440);
        settings.setHeight(810);
        settings.setVersion("1.0");
        settings.setGameMenuEnabled(false);
    }

    @Override
    protected void initGame() {
        // Set custom mouse cursor
        Image mouseCursorImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/Mouse_Cursor.png")).toExternalForm());
        WebSocketClientService webSocketClientService = new WebSocketClientService();

        getGameScene().setCursor(new ImageCursor(mouseCursorImage));

        SceneManager sceneManager = new SceneManager(webSocketClientService);
        sceneManager.showHomeScene(false);

        // Event handler for forcefully closing the app
        FXGL.getPrimaryStage().setOnCloseRequest(event -> {
            GameExit exit = new GameExit();
            exit.exit();
            event.consume();
        });
    }

    protected void initInput() {
        Input input = getInput();

        // Escaping to close the game
        input.addAction(new UserAction("Exit") {
            @Override
            protected void onActionEnd() {
                super.onActionEnd();
                GameExit exit = new GameExit();
                exit.exit();
            }
        }, KeyCode.ESCAPE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}