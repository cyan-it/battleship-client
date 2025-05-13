package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.utils.I18nLoader;
import com.battleshipclient.SceneManager;
import com.battleshipclient.utils.SimpleTextPopup;
import com.battleshipclient.UserOverlay;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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


public class LoginScene {

    private TextField usernameInput;
    private PasswordField passwordInput;

    private final Pane root;

    public LoginScene(SceneManager sceneManager) {
        VBox header = setHeaderBoxParameters(new VBox(20));
        VBox input = setLoginInputParameters(new VBox(40));
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
                Objects.requireNonNull(getClass().getResource("/assets/ui/login.css")).toExternalForm()
        );

        return  anchoredLayout;
    }

    // Sets the background image
    @NotNull
    private ImageView setBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/AccountScenes_Background.jpg")).toExternalForm());

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
        Text subTitle = new Text(I18nLoader.getText("scene.title.logIn"));
        subTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        subTitle.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, subTitle);

        return box;
    }

    // Sets the input fields and texts
    @NotNull
    private VBox setLoginInputParameters(@NotNull VBox box) {
        box.setAlignment(Pos.CENTER);

        HBox usernameBox = new HBox(20);
        usernameBox.setAlignment(Pos.CENTER);
        HBox passwordBox = new HBox(20);
        passwordBox.setAlignment(Pos.CENTER);

        // Create and format username title and input
        Text usernameTitle = new Text(I18nLoader.getText("username.title"));
        usernameTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));

        usernameInput = new TextField();
        usernameInput.getStyleClass().add("inputField");

        usernameBox.getChildren().addAll(usernameTitle, usernameInput);

        // Create and format password title and input
        Text passwordTitle = new Text(I18nLoader.getText("password.title"));
        passwordTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));

        passwordInput = new PasswordField();
        passwordInput.getStyleClass().add("inputField");

        passwordBox.getChildren().setAll(passwordTitle, passwordInput);

        box.getChildren().addAll(usernameBox, passwordBox);

        return box;
    }

    // Sets the login and cancel buttons
    @NotNull
    @Contract("_, _ -> param1")
    private HBox setNavigationButtons(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.BOTTOM_RIGHT);

        // Create and format login button
        Button toLoginButton = new Button(I18nLoader.getText("scene.title.logIn"));
        toLoginButton.setOnAction(_ -> verifyLogin(sceneManager));
        toLoginButton.getStyleClass().add("green-button");

        // Create and format cancel button
        Button toCancelButton = new Button(I18nLoader.getText("return"));
        toCancelButton.setOnAction(_ -> {
            sceneManager.showHomeScene(false);
            clearInputFields();
        });
        toCancelButton.getStyleClass().add("default-button");

        box.getChildren().addAll(toCancelButton, toLoginButton);

        return box;
    }

    private void verifyLogin(SceneManager sceneManager) {
        SimpleTextPopup loginErrorPopup = new SimpleTextPopup();
        Text errorText;

        // Check if username and password are filled
        if (usernameInput.getText().trim().isEmpty() || passwordInput.getText().trim().isEmpty()) {
            usernameInput.getStyleClass().add("inputField-error");
            passwordInput.getStyleClass().add("inputField-error");
            errorText = new Text(I18nLoader.getText("login.input.isMandatory"));
            loginErrorPopup.displayPopup(errorText);

        } else {
            // TODO: API-Call
            boolean apiCallReturn = true;
            if (apiCallReturn) {
                UserOverlay.initOverlay(true, usernameInput.getText().trim());
                sceneManager.showHomeScene(true);
                UserOverlay.showOverlay();
                clearInputFields();

            } else {
                usernameInput.getStyleClass().add("inputField-error");
                passwordInput.getStyleClass().add("inputField-error");
                errorText = new Text(I18nLoader.getText("login.input.isInvalid"));
                loginErrorPopup.displayPopup(errorText);
            }
        }
    }

    private void clearInputFields() {
        usernameInput.getStyleClass().remove("inputField-error");
        passwordInput.getStyleClass().remove("inputField-error");
        usernameInput.clear();
        passwordInput.clear();
    }

    public Pane getRoot() {
        return root;
    }
}
