package com.battleshipclient.scenes;

import com.battleshipclient.I18nLoader;
import com.battleshipclient.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;


public class CreateGameScene {

    // TODO: UserOverlay on init and exit!

    private final Pane root;

    public CreateGameScene(SceneManager sceneManager) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text(I18nLoader.getText("scene.title.createGame"));
        Button toHomeSceneButton = new Button(I18nLoader.getText("button.cancel"));

        toHomeSceneButton.setOnAction(event -> sceneManager.showHomeScene(true));

        layout.getChildren().addAll(title, toHomeSceneButton);
        this.root = layout;
    }

    public Pane getRoot() {
        return root;
    }
}
