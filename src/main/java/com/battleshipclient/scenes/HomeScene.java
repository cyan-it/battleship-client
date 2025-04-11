package com.battleshipclient.scenes;

import com.battleshipclient.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;


public class HomeScene {

    private final Pane root;

    public HomeScene(SceneManager sceneManager) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Text title = new Text("Home");
        Button toSignUpSceneButton = new Button("Sign Up");
        toSignUpSceneButton.setOnAction(event -> sceneManager.showSignUpScene());
        Button toLoginSceneButton = new Button("Login");
        toLoginSceneButton.setOnAction(event -> sceneManager.showLoginScene());
        Button toCreateGameSceneButton = new Button("Create Game");
        toCreateGameSceneButton.setOnAction(event -> sceneManager.showCreateGameScene());
        Button toJoinGameSceneButton = new Button("Join Game");
        toJoinGameSceneButton.setOnAction(event -> sceneManager.showJoinGameScene());

        layout.getChildren().addAll(title, toSignUpSceneButton, toLoginSceneButton, toCreateGameSceneButton, toJoinGameSceneButton);
        this.root = layout;
    }

    public Pane getRoot() {
        return root;
    }
}
