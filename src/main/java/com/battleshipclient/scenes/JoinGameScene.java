package com.battleshipclient.scenes;

import com.battleshipclient.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;


public class JoinGameScene {

    private final Pane root;

    public JoinGameScene(SceneManager sceneManager) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Join Game");
        Button toHomeSceneButton = new Button("Home");

        toHomeSceneButton.setOnAction(event -> sceneManager.showHomeScene());

        layout.getChildren().addAll(title, toHomeSceneButton);
        this.root = layout;
    }

    public Pane getRoot() {
        return root;
    }
}
