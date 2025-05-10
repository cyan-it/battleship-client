package com.battleshipclient.scenes.game;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.I18nLoader;
import com.battleshipclient.SceneManager;
import com.battleshipclient.SimpleConfirmationPopup;
import com.battleshipclient.UserOverlay;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class PlayGameScene {

    private final Pane root;

    public PlayGameScene(SceneManager sceneManager) {
        HBox users = setUserBoxParameters(new HBox());
        HBox fields = setFieldsBoxParameters(new HBox());
        HBox surrender = setSurrenderButton(new HBox(), sceneManager);

        this.root = setScene(users, fields, surrender);
    }

    private AnchorPane setScene(HBox users, HBox fields, HBox surrender) {
        AnchorPane anchoredLayout = new AnchorPane();

        anchoredLayout.getChildren().add(setBackgroundImage());
        anchoredLayout.getChildren().addAll(users, fields, surrender);

        AnchorPane.setTopAnchor(users, 20.0);
        AnchorPane.setLeftAnchor(users, 0.0);
        AnchorPane.setRightAnchor(users, 0.0);

        AnchorPane.setTopAnchor(fields, 200.0);
        AnchorPane.setLeftAnchor(fields, 0.0);
        AnchorPane.setRightAnchor(fields, 0.0);

        AnchorPane.setBottomAnchor(surrender, 20.0);
        AnchorPane.setRightAnchor(surrender, 20.0);

        anchoredLayout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        anchoredLayout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        anchoredLayout.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/inGame.css")).toExternalForm()
        );

        return anchoredLayout;
    }

    private ImageView setBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/GameBoard_Background.jpg")).toExternalForm());

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.fitWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        backgroundView.fitHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        return backgroundView;
    }

    private HBox setUserBoxParameters(HBox box) {
        // TODO: get Opponent name
        String opponentName = "Tobi";

        box.setAlignment(Pos.TOP_CENTER);

        Text title = new Text(UserOverlay.getUsername() + "  " + I18nLoader.getText("inGame.versus") + "  " + opponentName);
        title.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 60));
        title.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().add(title);

        return box;
    }

    private HBox setFieldsBoxParameters(HBox box) {
        // TODO: myBattleships / opponentsBattleships
        return box;
    }

    private HBox setSurrenderButton(HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.BOTTOM_RIGHT);

        Button toSurrenderButton = new Button(I18nLoader.getText("surrender"));
        toSurrenderButton.setOnAction(_ -> {
            Text confirmationText = new Text(I18nLoader.getText("surrender.confirm"));

            SimpleConfirmationPopup confirmationPopup = new SimpleConfirmationPopup();
            confirmationPopup.displayPopup(confirmationText, confirmed -> {
                if (confirmed) {
                    // TODO: Tell backend that surrendered
                    sceneManager.showHomeScene(true);
                    UserOverlay.showOverlay();
                }
            });
        });
        toSurrenderButton.getStyleClass().add("red-button");

        box.getChildren().addAll(toSurrenderButton);

        return box;
    }

    public Pane getRoot() {
        return root;
    }
}
