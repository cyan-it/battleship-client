package com.battleshipclient.utils;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class SimpleConfirmationPopup {

    public void displayPopup(@NotNull Text text, Consumer<Boolean> callback) {
        VBox popupBox = new VBox(20);
        popupBox.getStyleClass().add("simpleConfirmationPopup");

        HBox buttonBox = new HBox(40);
        buttonBox.setAlignment(Pos.BASELINE_CENTER);

        // Format confirmation text
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        // Add and format confirm and cancel buttons
        Button toConfirmButton = new Button(I18nLoader.getText("gameExit.yes"));
        toConfirmButton.getStyleClass().add("gameExit-confirm-button");
        Button toCancelButton = new Button(I18nLoader.getText("gameExit.no"));
        toCancelButton.getStyleClass().add("gameExit-cancel-button");

        // Button actions
        toConfirmButton.setOnAction(_ -> {
            FXGL.getGameScene().removeUINode(popupBox);
            callback.accept(true);
        });

        toCancelButton.setOnAction(_ -> {
            FXGL.getGameScene().removeUINode(popupBox);
            callback.accept(false);
        });

        // Add everything to popupBox
        buttonBox.getChildren().addAll(toConfirmButton, toCancelButton);
        popupBox.getChildren().addAll(text, buttonBox);

        // Load css
        popupBox.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/general.css")).toExternalForm()
        );

        // Center popup
        popupBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 250);
        popupBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 60);

        // Add popup to ui
        FXGL.getGameScene().addUINode(popupBox);
    }
}
