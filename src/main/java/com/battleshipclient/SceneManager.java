package com.battleshipclient;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.battleshipclient.scenes.*;
import javafx.scene.layout.Pane;

public class SceneManager {

     private final Pane homeScene;
     private final Pane homeSceneLoggedIn;
     private final Pane createGameScene;
     private final Pane joinGameScene;
     private final Pane loginScene;
     private final Pane signupScene;

     public SceneManager() {
          homeScene = new HomeScene(this).getRoot(false);
          homeSceneLoggedIn = new HomeScene(this).getRoot(true);
          createGameScene = new CreateGameScene(this).getRoot();
          joinGameScene = new JoinGameScene(this).getRoot();
          loginScene = new LoginScene(this).getRoot();
          signupScene = new SignUpScene(this).getRoot();
     }

     public void showHomeScene(boolean isLoggedIn) {
          getGameScene().clearUINodes();
          if (isLoggedIn) {
               getGameScene().addUINode(homeSceneLoggedIn);
          } else {
               getGameScene().addUINode(homeScene);
          }
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