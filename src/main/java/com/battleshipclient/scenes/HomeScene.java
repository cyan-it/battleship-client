package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.I18nLoader;
import com.battleshipclient.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeScene {

    private final Pane root;

    public HomeScene(SceneManager sceneManager) {
        VBox account = setAccountBoxParameters(new VBox(20), sceneManager);
        HBox game = setGameBoxParameters(new HBox(80), sceneManager);
        VBox header = setHeaderBoxParameters(new VBox(20), sceneManager);

        this.root = setScene(header, account, game);
    }

    @NotNull
    private AnchorPane setScene(VBox header, VBox account, HBox game) {
        AnchorPane anchoredLayout = new AnchorPane();

        anchoredLayout.getChildren().add(setBackgroundImage());
        anchoredLayout.getChildren().addAll(header, account, game);

        AnchorPane.setTopAnchor(header, 20.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        AnchorPane.setTopAnchor(account, 20.0);
        AnchorPane.setRightAnchor(account, 20.0);

        AnchorPane.setTopAnchor(game, 400.0);
        AnchorPane.setLeftAnchor(game, 0.0);
        AnchorPane.setRightAnchor(game, 0.0);

        anchoredLayout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        anchoredLayout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        anchoredLayout.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/style.css")).toExternalForm()
        );

        return anchoredLayout;
    }

    @NotNull
    private ImageView setBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/HomeScene_Background.jpg")).toExternalForm());

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.fitWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        backgroundView.fitHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        return backgroundView;
    }

    @NotNull
    @Contract("_, _ -> param1")
    private VBox setAccountBoxParameters(@NotNull VBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.TOP_RIGHT);

        Button toSignUpSceneButton = new Button(I18nLoader.getText("scene.title.signUp"));
        toSignUpSceneButton.setOnAction(event -> sceneManager.showSignUpScene());
        toSignUpSceneButton.getStyleClass().add("homeScene-account-button");

        Button toLoginSceneButton = new Button(I18nLoader.getText("scene.title.logIn"));
        toLoginSceneButton.setOnAction(event -> sceneManager.showLoginScene());
        toLoginSceneButton.getStyleClass().add("homeScene-account-button");

        box.getChildren().addAll(toLoginSceneButton, toSignUpSceneButton);

        return box;
    }

    @NotNull
    @Contract("_, _ -> param1")
    private HBox setGameBoxParameters(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.CENTER);

        Button toCreateGameSceneButton = new Button(I18nLoader.getText("scene.title.createGame"));
        toCreateGameSceneButton.setOnAction(event -> sceneManager.showCreateGameScene());
        toCreateGameSceneButton.getStyleClass().add("homeScene-game-button");

        Button toJoinGameSceneButton = new Button(I18nLoader.getText("scene.title.joinGame"));
        toJoinGameSceneButton.setOnAction(event -> sceneManager.showJoinGameScene());
        toJoinGameSceneButton.getStyleClass().add("homeScene-game-button");

        box.getChildren().addAll(toCreateGameSceneButton, toJoinGameSceneButton);

        return box;
    }

    @NotNull
    @Contract("_, _ -> param1")
    private VBox setHeaderBoxParameters(@NotNull VBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.TOP_CENTER);

        Text title = new Text(I18nLoader.getText("appTitle"));
        title.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 60));
        title.setTextAlignment(TextAlignment.CENTER);

        Text credentials = new Text(I18nLoader.getText("appCredentials"));
        credentials.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        credentials.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, credentials);

        return box;
    }

    public Pane getRoot() {
        return root;
    }
}
