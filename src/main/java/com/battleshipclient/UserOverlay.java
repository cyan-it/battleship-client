package com.battleshipclient;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class UserOverlay {

    private static HBox userOverlay;

    private static String username;
    public static boolean isLoggedIn;

    public static void initOverlay(boolean isVisible, String user) {
        if (isVisible && !isLoggedIn) {
            username = user;
            isLoggedIn = true;
        } else if (!isVisible && isLoggedIn) {
            username = "";
            isLoggedIn = false;
        }
    }

    public static void showOverlay() {
        userOverlay = new HBox(10);
        userOverlay.setAlignment(Pos.CENTER_LEFT);
        userOverlay.getStyleClass().add("userOverlay");
        userOverlay.setPrefWidth(Region.USE_COMPUTED_SIZE);
        userOverlay.setMinWidth(Region.USE_COMPUTED_SIZE);
        userOverlay.setMaxWidth(Region.USE_COMPUTED_SIZE);

        Text user = new Text(username);
        user.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
        user.setTextAlignment(TextAlignment.RIGHT);

        Image icon = new Image(Objects.requireNonNull(UserOverlay.class.getResource("/assets/textures/icons/icon_1.jpg")).toExternalForm());
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(50);
        iconView.setFitWidth(50);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.setCache(true);

        Circle clip = new Circle(25, 25, 25);
        iconView.setClip(clip);

        userOverlay.getChildren().addAll(iconView, user);

        userOverlay.getStylesheets().add(
                Objects.requireNonNull(UserOverlay.class.getResource("/assets/ui/general.css")).toExternalForm()
        );

        userOverlay.setTranslateX(0.0);
        userOverlay.setTranslateY(0.0);

        FXGL.getGameScene().addUINode(userOverlay);
    }

    public static void disableOverlay() {
        FXGL.getGameScene().removeUINode(userOverlay);
    }
}
