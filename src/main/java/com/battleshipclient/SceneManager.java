package com.battleshipclient;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.battleshipclient.scenes.*;
import javafx.scene.layout.Pane;

public class SceneManager {

     private final Pane homeScene;
     private final Pane createGameScene;
     private final Pane joinGameScene;
     private final Pane loginScene;
     private final Pane signupScene;

     public SceneManager() {
          homeScene = new HomeScene(this).getRoot();
          createGameScene = new CreateGameScene(this).getRoot();
          joinGameScene = new JoinGameScene(this).getRoot();
          loginScene = new LoginScene(this).getRoot();
          signupScene = new SignUpScene(this).getRoot();
     }

     public void showHomeScene() {
          getGameScene().clearUINodes();
          getGameScene().addUINode(homeScene);
     }

     public void showCreateGameScene() {
          getGameScene().clearUINodes();
          getGameScene().addUINode(createGameScene);
     }

     public void showJoinGameScene() {
          getGameScene().clearUINodes();
          getGameScene().addUINode(joinGameScene);
     }

     public void showLoginScene() {
          getGameScene().clearUINodes();
          getGameScene().addUINode(loginScene);
     }

     public void showSignUpScene() {
          getGameScene().clearUINodes();
          getGameScene().addUINode(signupScene);
     }
}