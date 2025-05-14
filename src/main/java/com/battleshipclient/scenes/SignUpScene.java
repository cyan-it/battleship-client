package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.ApiService;
import com.battleshipclient.status.UserStatus;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SignUpScene {

    private TextField usernameInput;
    private PasswordField passwordInput;
    private PasswordField passwordInputRepeat;

    private final Pane root;

    public SignUpScene(SceneManager sceneManager) {
        VBox header = setHeaderBoxParameters(new VBox(20));
        VBox input = setSignUpInputParameters(new VBox(40));
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
                Objects.requireNonNull(getClass().getResource("/assets/ui/signUp.css")).toExternalForm()
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
        Text subTitle = new Text(I18nLoader.getText("scene.title.signUp"));
        subTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        subTitle.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, subTitle);

        return box;
    }

    // Sets the input fields and texts
    @NotNull
    private VBox setSignUpInputParameters(@NotNull VBox box) {
        box.setAlignment(Pos.CENTER);

        HBox usernameBox = new HBox(20);
        usernameBox.setAlignment(Pos.CENTER);
        HBox passwordBox = new HBox(20);
        passwordBox.setAlignment(Pos.CENTER);
        HBox passwordRepeatBox = new HBox(20);
        passwordRepeatBox.setAlignment(Pos.CENTER);

        // Create and format username title and input
        Text usernameTitle = new Text(I18nLoader.getText("username.title"));
        usernameTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
        usernameTitle.setWrappingWidth(300);
        usernameTitle.setTextAlignment(TextAlignment.RIGHT);

        usernameInput = new TextField();
        usernameInput.getStyleClass().add("inputField");

        usernameBox.getChildren().addAll(usernameTitle, usernameInput);

        // Create and format password title and input
        Text passwordTitle = new Text(I18nLoader.getText("password.title"));
        passwordTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
        passwordTitle.setWrappingWidth(300);
        passwordTitle.setTextAlignment(TextAlignment.RIGHT);

        passwordInput = new PasswordField();
        passwordInput.getStyleClass().add("inputField");

        passwordBox.getChildren().setAll(passwordTitle, passwordInput);

        // Create and format repeat password title and input
        Text passwordRepeatTitle = new Text(I18nLoader.getText("passwordRepeat.title"));
        passwordRepeatTitle.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
        passwordRepeatTitle.setWrappingWidth(300);
        passwordRepeatTitle.setTextAlignment(TextAlignment.RIGHT);

        passwordInputRepeat = new PasswordField();
        passwordInputRepeat.getStyleClass().add("inputField");

        passwordRepeatBox.getChildren().setAll(passwordRepeatTitle, passwordInputRepeat);

        box.getChildren().addAll(usernameBox, passwordBox, passwordRepeatBox);

        return box;
    }

    // Sets the login and cancel buttons
    @NotNull
    @Contract("_, _ -> param1")
    private HBox setNavigationButtons(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.BOTTOM_RIGHT);

        // Create and format login button
        Button toLoginButton = new Button(I18nLoader.getText("scene.title.signUp"));
        toLoginButton.setOnAction(_ -> verifySignUp(sceneManager));
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

    private void verifySignUp(SceneManager sceneManager) {
        SimpleTextPopup signUpErrorPopup = new SimpleTextPopup();
        Text errorText;

        // Check if password and repeat password are equal
        if (!passwordInput.getText().trim().equals(passwordInputRepeat.getText().trim())) {
            usernameInput.getStyleClass().add("inputField-error");
            passwordInput.getStyleClass().add("inputField-error");
            passwordInputRepeat.getStyleClass().add("inputField-error");
            errorText = new Text(I18nLoader.getText("signUp.input.passwords.notEqual"));
            signUpErrorPopup.displayPopup(errorText);

        } else if (usernameInput.getText().trim().length() > 12) {
            usernameInput.getStyleClass().add("inputField-error");
            errorText = new Text(I18nLoader.getText("signUp.input.username.toLong"));
            signUpErrorPopup.displayPopup(errorText);

        } else {
            ApiService.registerUser(usernameInput.getText(), passwordInput.getText());

            if (!Objects.equals(UserStatus.getAccessToken(), "")) {
                UserOverlay.initOverlay(true, usernameInput.getText().trim());
                sceneManager.showHomeScene(true);
                UserOverlay.showOverlay();
                clearInputFields();

            } else {
                usernameInput.getStyleClass().add("inputField-error");
                errorText = new Text(I18nLoader.getText("signUp.input.username.exists"));
                signUpErrorPopup.displayPopup(errorText);
            }
        }
    }

    private void clearInputFields() {
        usernameInput.getStyleClass().remove("inputField-error");
        passwordInput.getStyleClass().remove("inputField-error");
        passwordInputRepeat.getStyleClass().remove("inputField-error");
        usernameInput.clear();
        passwordInput.clear();
        passwordInputRepeat.clear();
    }

    public Pane getRoot() {
        return root;
    }
}
