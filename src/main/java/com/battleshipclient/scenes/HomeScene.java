package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.I18nLoader;
import com.battleshipclient.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;


public class HomeScene {

    private final Pane root;

    public HomeScene(SceneManager sceneManager) {
        VBox layout = new VBox(300);
        layout.setAlignment(Pos.CENTER);

        VBox account = new VBox(20);
        account.setAlignment(Pos.TOP_RIGHT);
        VBox game = new VBox(20);
        game.setAlignment(Pos.BASELINE_CENTER);

        layout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        layout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        Text title = new Text(I18nLoader.getText("appTitle"));
        Button toSignUpSceneButton = new Button(I18nLoader.getText("scene.title.signUp"));
        toSignUpSceneButton.setOnAction(event -> sceneManager.showSignUpScene());
        Button toLoginSceneButton = new Button(I18nLoader.getText("scene.title.logIn"));
        toLoginSceneButton.setOnAction(event -> sceneManager.showLoginScene());
        Button toCreateGameSceneButton = new Button(I18nLoader.getText("scene.title.createGame"));
        toCreateGameSceneButton.setOnAction(event -> sceneManager.showCreateGameScene());
        Button toJoinGameSceneButton = new Button(I18nLoader.getText("scene.title.joinGame"));
        toJoinGameSceneButton.setOnAction(event -> sceneManager.showJoinGameScene());

        account.getChildren().addAll(toLoginSceneButton, toSignUpSceneButton);
        game.getChildren().addAll(toCreateGameSceneButton, toJoinGameSceneButton);

        layout.getChildren().addAll(title, account, game);
        this.root = layout;
    }

    public Pane getRoot() {
        return root;
    }
}
