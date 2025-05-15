package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.ApiService;
import com.battleshipclient.SceneManager;
import com.battleshipclient.UserOverlay;
import com.battleshipclient.WebSocketClientService;
import com.battleshipclient.status.GameStatus;
import com.battleshipclient.utils.I18nLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class CreateGameScene {

    private final SceneManager sceneManager;
    @NotNull
    private final WebSocketClientService webSocketService;
    private final Pane root;
    private TextField gameKeyInput;
    private VBox loadingOverlay;

    public CreateGameScene(SceneManager sceneManager, WebSocketClientService webSocketService) {
        this.sceneManager = sceneManager;
        this.webSocketService = webSocketService;
        VBox header = setHeaderBoxParameters(new VBox(20));
        VBox keyDisplay = setKeyParameter(new VBox(40));
        HBox navigation = setNavigationButtons(new HBox(40), sceneManager);

        this.root = setScene(header, keyDisplay, navigation);

        webSocketService.setGameScene(this);
    }

    @NotNull
    // Sets the whole UI
    private AnchorPane setScene(VBox header, VBox keyDisplay, HBox navigation) {
        AnchorPane anchoredLayout = new AnchorPane();

        anchoredLayout.getChildren().add(setBackgroundImage());
        anchoredLayout.getChildren().addAll(header, keyDisplay, navigation);

        // Set position for header box
        AnchorPane.setTopAnchor(header, 20.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        // Set position for navigation buttons
        AnchorPane.setBottomAnchor(navigation, 20.0);
        AnchorPane.setRightAnchor(navigation, 20.0);

        // Set position for keyDisplay field
        AnchorPane.setLeftAnchor(keyDisplay, 0.0);
        AnchorPane.setRightAnchor(keyDisplay, 0.0);
        AnchorPane.setTopAnchor(keyDisplay, 100.0);
        AnchorPane.setBottomAnchor(keyDisplay, 50.0);

        // Set to fullscreen
        anchoredLayout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        anchoredLayout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        // Load css
        anchoredLayout.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/createGame.css")).toExternalForm()
        );

        return anchoredLayout;
    }

    // Sets the background image
    @NotNull
    private ImageView setBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/PreGameScenes_Background.jpg")).toExternalForm());

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.fitWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        backgroundView.fitHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        return backgroundView;
    }

    // Sets the title and subtitle
    @NotNull
    @Contract("_ -> param1")
    private VBox setHeaderBoxParameters(@NotNull VBox box) {
        box.setAlignment(Pos.TOP_CENTER);

        // Create and format title
        Text title = new Text(I18nLoader.getText("appTitle"));
        title.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 60));
        title.setTextAlignment(TextAlignment.CENTER);

        // Create and format subTitle
        Text subTitle = new Text(I18nLoader.getText("scene.title.createGame"));
        subTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        subTitle.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, subTitle);

        return box;
    }

    // Sets the keyDisplay field and text
    @NotNull
    private VBox setKeyParameter(@NotNull VBox box) {
        box.setAlignment(Pos.CENTER);

        HBox gameKeyBox = new HBox(20);
        gameKeyBox.setAlignment(Pos.CENTER);

        // Create and format gameKey title and display
        Text gameKeyTitle = new Text(I18nLoader.getText("gameKey.title"));
        gameKeyTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));

        gameKeyInput = new TextField();
        gameKeyInput.getStyleClass().add("inputField");
        gameKeyInput.setEditable(false);

        gameKeyBox.getChildren().setAll(gameKeyTitle, gameKeyInput);

        box.getChildren().addAll(gameKeyBox);

        return box;
    }

    // Sets the login and cancel buttons
    @NotNull
    @Contract("_, _ -> param1")
    private HBox setNavigationButtons(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.BOTTOM_RIGHT);

        // Create and format createGame button
        Button toCreateGameButton = new Button(I18nLoader.getText("create"));
        toCreateGameButton.setOnAction(_ -> createGame());
        toCreateGameButton.getStyleClass().add("green-button");

        // Create and format cancel button
        Button toCancelButton = new Button(I18nLoader.getText("return"));
        toCancelButton.setOnAction(_ -> {
            sceneManager.showHomeScene(true);
            clearInputFields();
            UserOverlay.showOverlay();
        });
        toCancelButton.getStyleClass().add("default-button");

        box.getChildren().addAll(toCancelButton, toCreateGameButton);

        return box;
    }

    private void createGame() {
        ApiService.createGame();
        gameKeyInput.setText(GameStatus.getGameKey());
        showLoadingOverlay();
    }

    public void afterPlayerJoined(String opponentName) {
        FXGL.getGameScene().removeUINode(loadingOverlay);
        GameStatus.setOpponentUserName(opponentName);
        GameStatus.startGame(true);
        PlayGameScene playGame = new PlayGameScene(sceneManager, webSocketService);
        FXGL.getGameScene().clearUINodes();
        FXGL.getGameScene().addUINode(playGame.getRoot());
    }

    private void showLoadingOverlay() {
        loadingOverlay = new VBox(40);
        loadingOverlay.setAlignment(Pos.CENTER);

        Text waitingForOpponentText = new Text(I18nLoader.getText("waitingForOpponent"));
        waitingForOpponentText.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
        waitingForOpponentText.setTextAlignment(TextAlignment.CENTER);
        waitingForOpponentText.getStyleClass().add("yellow-text");

        Image loadingIcon = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/LoadingIcon.png")).toExternalForm());
        ImageView loadingIconView = new ImageView(loadingIcon);
        loadingIconView.setFitHeight(100.0);
        loadingIconView.setFitWidth(100.0);

        loadingOverlay.getChildren().setAll(waitingForOpponentText, loadingIconView);
        loadingOverlay.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/createGame.css")).toExternalForm()
        );
        loadingOverlay.setTranslateX(FXGL.getAppWidth() / 2.0 - 175);
        loadingOverlay.setTranslateY(FXGL.getAppHeight() / 2.0 - 220);

        FXGL.getGameScene().addUINode(loadingOverlay);
    }

    private void clearInputFields() {
        gameKeyInput.getStyleClass().remove("inputField-error");
        gameKeyInput.clear();
    }

    public Pane getRoot() {
        return root;
    }
}
