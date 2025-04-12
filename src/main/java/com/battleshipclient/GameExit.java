package com.battleshipclient;

import com.almasb.fxgl.dsl.FXGL;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

import java.util.Objects;
import java.util.function.Consumer;

public class GameExit {

    public void exit() {
        // TODO: Maybe do some stuff before exiting - later on
        showConfirmationPopup(confirmed -> {
            if (confirmed) Platform.exit();
        });
    }

    private void showConfirmationPopup(Consumer<Boolean> callback) {
        VBox popupBox = new VBox(20);
        popupBox.getStyleClass().add("gameExit-popup");

        HBox buttonBox = new HBox(40);
        buttonBox.setAlignment(Pos.BASELINE_CENTER);

        Text confirmationText = new Text(I18nLoader.getText("gameExit.confirmationText"));
        confirmationText.setTextAlignment(TextAlignment.CENTER);
        confirmationText.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        Button toConfirmButton = new Button(I18nLoader.getText("gameExit.yes"));
        toConfirmButton.getStyleClass().add("gameExit-confirm-button");
        Button toCancelButton = new Button(I18nLoader.getText("gameExit.no"));
        toCancelButton.getStyleClass().add("gameExit-cancel-button");

        toConfirmButton.setOnAction(actionEvent -> {
            FXGL.getGameScene().removeUINode(popupBox);
            callback.accept(true);
        });

        toCancelButton.setOnAction(actionEvent -> {
            FXGL.getGameScene().removeUINode(popupBox);
            callback.accept(false);
        });

        buttonBox.getChildren().addAll(toConfirmButton, toCancelButton);
        popupBox.getChildren().addAll(confirmationText, buttonBox);

        popupBox.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/style.css")).toExternalForm()
        );

        popupBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 250);
        popupBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 60);

        FXGL.getGameScene().addUINode(popupBox);
    }
}
