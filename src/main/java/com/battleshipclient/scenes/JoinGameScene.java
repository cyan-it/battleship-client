package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.status.GameStatus;
import com.battleshipclient.utils.I18nLoader;
import com.battleshipclient.SceneManager;
import com.battleshipclient.utils.SimpleTextPopup;
import com.battleshipclient.UserOverlay;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class JoinGameScene {

    private TextField gameKeyInput;

    private final Pane root;

    public JoinGameScene(SceneManager sceneManager) {
        VBox header = setHeaderBoxParameters(new VBox(20));
        VBox input = setKeyParameter(new VBox(40));
        HBox navigation = setNavigationButtons(new HBox(40), sceneManager);

        this.root = setScene(header, input, navigation);
    }

    @NotNull
    // Sets the whole UI
    private AnchorPane setScene(VBox header, VBox input, HBox navigation) {
        AnchorPane anchoredLayout = new AnchorPane();

        anchoredLayout.getChildren().add(setBackgroundImage());
        anchoredLayout.getChildren().addAll(header, input, navigation);

        // Set position for header box
        AnchorPane.setTopAnchor(header, 20.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        // Set position for navigation buttons
        AnchorPane.setBottomAnchor(navigation, 20.0);
        AnchorPane.setRightAnchor(navigation, 20.0);

        // Set position for input fields
        AnchorPane.setLeftAnchor(input, 0.0);
        AnchorPane.setRightAnchor(input, 0.0);
        AnchorPane.setTopAnchor(input, 100.0);
        AnchorPane.setBottomAnchor(input, 50.0);

        // Set to fullscreen
        anchoredLayout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        anchoredLayout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        // Load css
        anchoredLayout.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/joinGame.css")).toExternalForm()
        );

        return  anchoredLayout;
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
        Text subTitle = new Text(I18nLoader.getText("scene.title.joinGame"));
        subTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        subTitle.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, subTitle);

        return box;
    }

    // Sets the input field and text
    @NotNull
    private VBox setKeyParameter(@NotNull VBox box) {
        box.setAlignment(Pos.CENTER);

        HBox gameKeyBox = new HBox(20);
        gameKeyBox.setAlignment(Pos.CENTER);

        // Create and format gameKey title and input
        Text gameKeyTitle = new Text(I18nLoader.getText("gameKey.title"));
        gameKeyTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));

        gameKeyInput = new TextField();
        gameKeyInput.getStyleClass().add("inputField");

        gameKeyBox.getChildren().setAll(gameKeyTitle, gameKeyInput);

        box.getChildren().addAll(gameKeyBox);

        return box;
    }

    // Sets the login and cancel buttons
    @NotNull
    @Contract("_, _ -> param1")
    private HBox setNavigationButtons(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.BOTTOM_RIGHT);

        // Create and format joinGame button
        Button toJoinGameButton = new Button(I18nLoader.getText("joinGame"));
        toJoinGameButton.setOnAction(_ -> verifyKeyAndStartGame(sceneManager));
        toJoinGameButton.getStyleClass().add("green-button");

        // Create and format cancel button
        Button toCancelButton = new Button(I18nLoader.getText("return"));
        toCancelButton.setOnAction(_ -> {
            clearInputFields();
            sceneManager.showHomeScene(true);
            UserOverlay.showOverlay();
        });
        toCancelButton.getStyleClass().add("default-button");

        box.getChildren().addAll(toCancelButton, toJoinGameButton);

        return box;
    }

    private void verifyKeyAndStartGame(SceneManager sceneManager) {
        // TODO: verify if key valid
        if (true) {
            GameStatus.startGame(false);
            PlayGameScene playGame = new PlayGameScene(sceneManager);
            FXGL.getGameScene().clearUINodes();
            FXGL.getGameScene().addUINode(playGame.getRoot());
        } else {
            gameKeyInput.getStyleClass().add("inputField-error");

            SimpleTextPopup invalidKeyErrorPopup = new SimpleTextPopup();
            Text errorText = new Text(I18nLoader.getText("joinGame.input.key.invalid"));
            invalidKeyErrorPopup.displayPopup(errorText);
        }
    }

    private void clearInputFields() {
        gameKeyInput.getStyleClass().remove("inputField-error");
        gameKeyInput.clear();
    }
    
    public Pane getRoot() {
        return root;
    }
}
