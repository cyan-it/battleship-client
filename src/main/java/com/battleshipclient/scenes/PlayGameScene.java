package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.status.GameStatus;
import com.battleshipclient.status.UserStatus;
import com.battleshipclient.utils.I18nLoader;
import com.battleshipclient.SceneManager;
import com.battleshipclient.utils.SimpleConfirmationPopup;
import com.battleshipclient.UserOverlay;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlayGameScene {

    private GridPane myBoard;

    private Button toMakeHit;
    private StackPane selectedCell = null;
    private Circle currentDot = null;
    private final Set<StackPane> lockedCells = new HashSet<>();

    private Text notificationText;

    private ImageView shipImageView;
    private Text counterText;
    private int shipSize;
    private boolean isShipVertical;
    private boolean isShipPlaced;
    private int shipRow;
    private int shipCol;

    private final Pane root;

    public PlayGameScene(SceneManager sceneManager) {
        UserStatus.setInGameStatus(true);
        VBox header = setHeaderBoxParameters(new VBox(20));
        HBox fields = setFieldsBoxParameters(new HBox(200));
        HBox ships = setShipPlaceholder(new HBox(10));
        HBox actionButtons = setActionButtons(new HBox(40), sceneManager);

        this.root = setScene(header, fields, actionButtons, ships);

        placeShips(2);

        setOpponentTurnNotification(GameStatus.getIsMyTurnValue());
        GameStatus.getIsMyTurn().addListener((_, _, newVal) -> setOpponentTurnNotification(newVal));
    }

    @NotNull
    private AnchorPane setScene(VBox header, HBox fields, HBox actionButtons, HBox ships) {
        AnchorPane anchoredLayout = new AnchorPane();

        anchoredLayout.getChildren().add(setBackgroundImage());
        anchoredLayout.getChildren().addAll(header, fields, actionButtons, ships);

        AnchorPane.setTopAnchor(header, 20.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        AnchorPane.setTopAnchor(fields, 200.0);
        AnchorPane.setLeftAnchor(fields, 0.0);
        AnchorPane.setRightAnchor(fields, 0.0);

        AnchorPane.setBottomAnchor(actionButtons, 20.0);
        AnchorPane.setRightAnchor(actionButtons, 20.0);

        AnchorPane.setLeftAnchor(ships, 300.0);
        AnchorPane.setBottomAnchor(ships, 40.0);

        anchoredLayout.prefWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        anchoredLayout.prefHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        anchoredLayout.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/assets/ui/inGame.css")).toExternalForm()
        );

        return anchoredLayout;
    }

    @NotNull
    private ImageView setBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/GameBoard_Background.jpg")).toExternalForm());

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.fitWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
        backgroundView.fitHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());

        return backgroundView;
    }

    @NotNull
    @Contract("_ -> param1")
    private VBox setHeaderBoxParameters(@NotNull VBox box) {
        // TODO: get Opponent name
        String opponentName = "Tobi";

        box.setAlignment(Pos.TOP_CENTER);

        Text title = new Text(UserStatus.getUsername() + "  " + I18nLoader.getText("inGame.versus") + "  " + opponentName);
        title.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 60));
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("board-text");

        notificationText = new Text();
        notificationText.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 30));
        notificationText.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(title, notificationText);

        return box;
    }

    @NotNull
    @Contract("_ -> param1")
    private HBox setFieldsBoxParameters(@NotNull HBox box) {
        box.setAlignment(Pos.CENTER);

        VBox myBoard = new VBox(10);
        myBoard.setAlignment(Pos.CENTER);
        Text myBoardTitle = new Text(I18nLoader.getText("inGame.myBoard"));
        myBoardTitle.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));
        myBoardTitle.setTextAlignment(TextAlignment.CENTER);
        myBoardTitle.getStyleClass().add("board-text");

        myBoard.getChildren().addAll(setMyBoard(), myBoardTitle);

        VBox opponentBoard = new VBox(10);
        opponentBoard.setAlignment(Pos.CENTER);
        Text opponentBoardTitle = new Text(I18nLoader.getText("inGame.opponentBoard"));
        opponentBoardTitle.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));
        opponentBoardTitle.setTextAlignment(TextAlignment.CENTER);
        opponentBoardTitle.getStyleClass().add("board-text");

        opponentBoard.getChildren().addAll(setOpponentBoard(), opponentBoardTitle);

        box.getChildren().addAll(myBoard, opponentBoard);

        return box;
    }

    private GridPane setMyBoard() {
        myBoard = new GridPane();

        for (int col = 0; col <= 10; col++) {
            for (int row = 0; row <= 10; row++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(40, 40);
                cell.getStyleClass().add("board-cell");

                if (row == 0 && col == 0) {
                    cell.getStyleClass().remove("board-cell");
                } else if (row == 0) {
                    cell.getStyleClass().remove("board-cell");
                    Label label = new Label(String.valueOf(col));
                    label.getStyleClass().add("board-label");
                    cell.getChildren().add(label);

                } else if (col == 0) {
                    cell.getStyleClass().remove("board-cell");
                    Label label = new Label(String.valueOf((char) ('A' + row - 1)));
                    label.getStyleClass().add("board-label");
                    cell.getChildren().add(label);
                } else {
                    final int selectedCol = col;
                    final int selectedRow = row;
                    cell.setOnMouseClicked(_ -> {
                        boolean outOfBounds = isShipVertical
                                ? selectedRow + shipSize - 1 > 10
                                : selectedCol + shipSize - 1 > 10;

                        boolean canPlace = GameStatus.canPlaceShip(
                                shipSize, isShipVertical, selectedCol - 1, selectedRow - 1);

                        if (outOfBounds || !canPlace) {
                            notificationText.setFill(Color.RED);
                            notificationText.setText(I18nLoader.getText("inGame.notification.shipPlacement.invalid"));
                        } else {
                            shipRow = selectedRow;
                            shipCol = selectedCol;
                            isShipPlaced = true;
                        }
                    });
                }

                myBoard.add(cell, col, row);
            }
        }

        return myBoard;
    }

    private GridPane setOpponentBoard() {
        GridPane opponentBoard = new GridPane();

        for (int col = 0; col <= 10; col++) {
            for (int row = 0; row <= 10; row++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(40, 40);
                cell.getStyleClass().add("board-cell");

                if (row == 0 && col == 0) {
                    cell.getStyleClass().remove("board-cell");
                } else if (row == 0) {
                    cell.getStyleClass().remove("board-cell");
                    Label label = new Label(String.valueOf(col));
                    label.getStyleClass().add("board-label");
                    cell.getChildren().add(label);

                } else if (col == 0) {
                    cell.getStyleClass().remove("board-cell");
                    Label label = new Label(String.valueOf((char) ('A' + row - 1)));
                    label.getStyleClass().add("board-label");
                    cell.getChildren().add(label);
                } else {
                    cell.setOnMouseClicked(_ -> {
                        if (lockedCells.contains(cell) || !GameStatus.getIsMyTurnValue() || !GameStatus.allShipsSet()) {
                            return;
                        }

                        if (selectedCell != null && currentDot != null) {
                            selectedCell.getChildren().remove(currentDot);
                        }

                        selectedCell = cell;
                        currentDot = new Circle(10, Color.GREY);
                        cell.getChildren().add(currentDot);

                        toMakeHit.setDisable(false);
                    });
                }

                opponentBoard.add(cell, col, row);
            }
        }

        return opponentBoard;
    }

    @NotNull
    @Contract("_, _ -> param1")
    private HBox setActionButtons(@NotNull HBox box, SceneManager sceneManager) {
        box.setAlignment(Pos.BOTTOM_RIGHT);

        Button toSurrenderButton = new Button(I18nLoader.getText("inGame.surrender"));
        toSurrenderButton.setOnAction(_ -> {
            Text confirmationText = new Text(I18nLoader.getText("inGame.surrender.confirm"));

            SimpleConfirmationPopup confirmationPopup = new SimpleConfirmationPopup();
            confirmationPopup.displayPopup(confirmationText, confirmed -> {
                if (confirmed) {
                    // TODO: Tell backend that surrendered
                    UserStatus.setInGameStatus(false);
                    sceneManager.showHomeScene(true);
                    UserOverlay.showOverlay();
                }
            });
        });
        toSurrenderButton.getStyleClass().add("red-button");

        toMakeHit = new Button(I18nLoader.getText("inGame.makeHit"));
        toMakeHit.setOnAction(_ -> {
            if (selectedCell != null & currentDot != null) {
                // TODO: Check if Hit in backend
                boolean isHit = true;

                if (isHit) {
                    notificationText.setText(I18nLoader.getText("inGame.notification.hit"));
                    notificationText.setFill(Color.LIGHTGREEN);
                } else {
                    notificationText.setText(I18nLoader.getText("inGame.notification.noHit"));
                    notificationText.setFill(Color.YELLOW);
                }
                currentDot.setFill(isHit ? Color.RED : Color.WHITE);
                lockedCells.add(selectedCell);

                selectedCell = null;
                currentDot = null;

                PauseTransition notificationTextPause = new PauseTransition(Duration.seconds(3));
                notificationTextPause.setOnFinished(_ -> {
                    notificationText.setText("");
                    GameStatus.setOpponentTurn();
                });
                notificationTextPause.play();

                toMakeHit.setDisable(true);
            }
        });
        toMakeHit.getStyleClass().add("green-button");
        toMakeHit.setDisable(true);
        toMakeHit.visibleProperty().bind(GameStatus.getIsMyTurn());

        box.getChildren().addAll(toMakeHit, toSurrenderButton);

        return box;
    }

    private void setOpponentTurnNotification(boolean isMyTurn) {
        if (!isMyTurn) {
            notificationText.setFill(Color.RED);
            notificationText.setText(I18nLoader.getText("inGame.notification.opponentTurn"));
        }
    }

    private HBox setShipPlaceholder(HBox box) {
        shipImageView = new ImageView();

        VBox counterBox = new VBox();
        counterBox.setAlignment(Pos.BOTTOM_CENTER);

        counterText = new Text();
        counterText.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));
        counterText.setTextAlignment(TextAlignment.CENTER);
        counterText.getStyleClass().add("board-text");

        counterBox.getChildren().add(counterText);

        box.getChildren().addAll(shipImageView, counterBox);

        return box;
    }

//    private void placeShips() {
//        for (int size = 2; size <= 5;) {
//            shipSize = size;
//            int counter;
//            switch (size) {
//                case 2:
//                    counter = GameStatus.getNumberOfSize2ShipsToBeSet();
//                    break;
//                case 3:
//                    counter = GameStatus.getNumberOfSize3ShipsToBeSet();
//                    break;
//                case 4:
//                    counter = GameStatus.getNumberOfSize4ShipsToBeSet();
//                    break;
//                case 5:
//                    counter = GameStatus.getNumberOfSize5ShipsToBeSet();
//                    break;
//                default:
//                    counter = 0;
//            }
//
//            if (counter == 0) {
//                size++;
//            } else {
//                Image shipImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/ships/size_" + size + "_ship.png")).toExternalForm());
//                shipImageView.setImage(shipImage);
//                shipImageView.setFitWidth(40*size);
//                shipImageView.setPreserveRatio(true);
//
//                counterText.setText(counter + "X");
//
//                // Wait for isShipPlaced = true
//            };
//        }
//    }

    private void placeShips(int size) {
        if (size > 5) return; // done placing

        int counter;
        switch (size) {
            case 2 -> counter = GameStatus.getNumberOfSize2ShipsToBeSet();
            case 3 -> counter = GameStatus.getNumberOfSize3ShipsToBeSet();
            case 4 -> counter = GameStatus.getNumberOfSize4ShipsToBeSet();
            case 5 -> counter = GameStatus.getNumberOfSize5ShipsToBeSet();
            default -> counter = 0;
        }

        if (counter == 0) {
            placeShips(size + 1);
        } else {
            shipSize = size;
            Image shipImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/ships/size_" + size + "_ship.png")).toExternalForm());
            shipImageView.setImage(shipImage);
            shipImageView.setFitWidth(40 * size);
            shipImageView.setFitHeight(40);

            ImageView shipImageInBoard = new ImageView(shipImage);
            shipImageInBoard.setFitWidth(40 * size);
            shipImageInBoard.setFitHeight(40);

            counterText.setText(counter + "X");

            isShipPlaced = false;

            // Poll every 100ms to check if the ship has been placed
            Timeline waitForPlacement = new Timeline();
            waitForPlacement.getKeyFrames().add(new KeyFrame(Duration.millis(100), _ -> {
                if (isShipPlaced) {
                    waitForPlacement.stop();

                    int posY = isShipVertical ? 1 : shipSize;
                    int posX = isShipVertical ? shipSize : 1;

                    myBoard.add(shipImageInBoard, shipCol, shipRow, posY, posX);

                    GameStatus.placeShip(shipSize, isShipVertical, shipCol - 1, shipRow - 1);
                    // TODO: Grids werden immer weiter nach außen verschoben
                    placeShips(size); // repeat for same size if needed
                }
            }));
            waitForPlacement.setCycleCount(Animation.INDEFINITE);
            waitForPlacement.play();
        }
    }


    public Pane getRoot() {
        return root;
    }
}