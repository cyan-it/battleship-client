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
        VBox header = setHeaderBoxParameters(new VBox(20));

        this.root = setScene(header, account, game);
    }

    @NotNull
    // Sets the whole UI
    private AnchorPane setScene(VBox header, VBox account, HBox game) {
        AnchorPane anchoredLayout = new AnchorPane();

        anchoredLayout.getChildren().add(setBackgroundImage());
        anchoredLayout.getChildren().addAll(header, account, game);

        // Set position for header box
        AnchorPane.setTopAnchor(header, 20.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        // Set position for account box
        AnchorPane.setTopAnchor(account, 20.0);
        AnchorPane.setRightAnchor(account, 20.0);

        // Set position for game box
        AnchorPane.setTopAnchor(game, 400.0);
        AnchorPane.setLeftAnchor(game, 0.0);
        AnchorPane.setRightAnchor(game, 0.0);

        // Set to fullscreen
        anchoredLayout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        anchoredLayout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        // Load css
        anchoredLayout.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/home.css")).toExternalForm()
        );

        return anchoredLayout;
    }

    @NotNull
    // Sets the background image
    private ImageView setBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/HomeScene_Background.jpg")).toExternalForm());

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.fitWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        backgroundView.fitHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        return backgroundView;
    }

    @NotNull
    @Contract("_, _ -> param1")
    // Sets the Login and Sign Up buttons
    private VBox setAccountBoxParameters(@NotNull VBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.TOP_RIGHT);

        // Create and format signUp Button
        Button toSignUpSceneButton = new Button(I18nLoader.getText("scene.title.signUp"));
        toSignUpSceneButton.setOnAction(_ -> sceneManager.showSignUpScene());
        toSignUpSceneButton.getStyleClass().add("account-button");

        // Create and format logIn Button
        Button toLoginSceneButton = new Button(I18nLoader.getText("scene.title.logIn"));
        toLoginSceneButton.setOnAction(_ -> sceneManager.showLoginScene());
        toLoginSceneButton.getStyleClass().add("account-button");

        box.getChildren().addAll(toLoginSceneButton, toSignUpSceneButton);

        return box;
    }

    @NotNull
    @Contract("_, _ -> param1")
    // Sets the Create Game and Join Game buttons
    private HBox setGameBoxParameters(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.CENTER);

        // Create and format createGame Button
        Button toCreateGameSceneButton = new Button(I18nLoader.getText("scene.title.createGame"));
        toCreateGameSceneButton.setOnAction(_ -> sceneManager.showCreateGameScene());
        toCreateGameSceneButton.getStyleClass().add("game-button");

        // Create and format joinGame Button
        Button toJoinGameSceneButton = new Button(I18nLoader.getText("scene.title.joinGame"));
        toJoinGameSceneButton.setOnAction(_ -> sceneManager.showJoinGameScene());
        toJoinGameSceneButton.getStyleClass().add("game-button");

        box.getChildren().addAll(toCreateGameSceneButton, toJoinGameSceneButton);

        return box;
    }

    @NotNull
    @Contract("_ -> param1")
    // Sets the title and credentials
    private VBox setHeaderBoxParameters(@NotNull VBox box) {
        box.setAlignment(Pos.TOP_CENTER);

        // Create and format title
        Text title = new Text(I18nLoader.getText("appTitle"));
        title.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 60));
        title.setTextAlignment(TextAlignment.CENTER);

        // Create and format credentials
        Text credentials = new Text(I18nLoader.getText("appCredentials"));
        credentials.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        credentials.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, credentials);

        return box;
    }

    // Returns the home scene
    public Pane getRoot() {
        return root;
    }
}
