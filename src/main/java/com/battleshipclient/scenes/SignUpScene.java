package com.battleshipclient.scenes;

import com.battleshipclient.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

public class SignUpScene {

    private final Pane root;

    public SignUpScene(SceneManager sceneManager) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Sign Up");
        Button toHomeSceneButton = new Button("Home");

        toHomeSceneButton.setOnAction(event -> sceneManager.showHomeScene());

        layout.getChildren().addAll(title, toHomeSceneButton);
        this.root = layout;
    }

    public Pane getRoot() {
        return root;
    }
}
