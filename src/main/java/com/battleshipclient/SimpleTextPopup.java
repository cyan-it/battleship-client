package com.battleshipclient;

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

public class SimpleTextPopup {

    public void displayPopup(@NotNull Text text) {
        VBox popupBox = new VBox(20);
        popupBox.getStyleClass().add("simpleTextPopup");

        HBox closeButton = new HBox();
        closeButton.setAlignment(Pos.BASELINE_CENTER);

        // Add and format text field
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

        // Add and format close button
        Button toCloseButton = new Button(I18nLoader.getText("close"));
        toCloseButton.getStyleClass().add("simpleTextPopup-close-button");

        // Close popup after button-press
        toCloseButton.setOnAction(_ -> FXGL.getGameScene().removeUINode(popupBox));

        closeButton.getChildren().add(toCloseButton);
        popupBox.getChildren().addAll(text, closeButton);

        // Load css
        popupBox.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/general.css")).toExternalForm()
        );

        // Center popup
        popupBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 250);
        popupBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 50);

        // Add popup to ui
        FXGL.getGameScene().addUINode(popupBox);
    }
}
