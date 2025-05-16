package com.battleshipclient.scenes;

import com.almasb.fxgl.dsl.FXGL;
import com.battleshipclient.ApiService;
import com.battleshipclient.SceneManager;
import com.battleshipclient.UserOverlay;
import com.battleshipclient.WebSocketClientService;
import com.battleshipclient.enums.HitType;
import com.battleshipclient.status.GameStatus;
import com.battleshipclient.status.UserStatus;
import com.battleshipclient.utils.I18nLoader;
import com.battleshipclient.utils.SimpleConfirmationPopup;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    private final SceneManager sceneManager;
    private final Set<StackPane> lockedCells = new HashSet<>();
    private final Pane root;
    private GridPane myBoard;
    private Button toMakeHit;
    private StackPane selectedCell = null;
    private Circle currentDot = null;
    private int hitPosX;
    private int hitPosY;
    private Text notificationText;
    private ImageView imageViewOfShipOfCurrentType;
    private Text counterTextOfShipsOfCurrentType;
    private int currentSelectedShipSize;
    private boolean currentSelectedShipIsVertical;
    private boolean currentSelectedShipIsPlaced;
    private int currentPlacedShipRow;
    private int currentPlacedShipCol;

    public PlayGameScene(SceneManager sceneManager, WebSocketClientService webSocketService) {
        this.sceneManager = sceneManager;
        UserStatus.setInGameStatus(true);
        VBox header = setHeaderBoxParameters(new VBox(20));
        HBox fields = setFieldsBoxParameters(new HBox(200));
        HBox ships = setShipPlaceholder(new HBox(30));
        HBox actionButtons = setActionButtons(new HBox(40), sceneManager);

        this.root = setScene(header, fields, actionButtons, ships);

        placeShips(2);

        setOpponentTurnNotification(GameStatus.getIsMyTurnValue());
        GameStatus.getIsMyTurn().addListener((ignore, ignoredValue, newVal) -> setOpponentTurnNotification(newVal));

        webSocketService.setPlayGameScene(this);
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
        box.setAlignment(Pos.TOP_CENTER);

        Text title = new Text(UserStatus.getUsername() + "  " + I18nLoader.getText("inGame.versus") + "  " + GameStatus.getOpponentUserName());
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

            ColumnConstraints colConstraints = new ColumnConstraints(40);
            colConstraints.setFillWidth(true);
            colConstraints.setHgrow(Priority.NEVER);
            myBoard.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints(40);
            rowConstraints.setFillHeight(true);
            rowConstraints.setVgrow(Priority.NEVER);
            myBoard.getRowConstraints().add(rowConstraints);

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
                    cell.setOnMouseClicked(event -> {
                        if (!GameStatus.allShipsSet()) {
                            boolean outOfBounds = currentSelectedShipIsVertical
                                    ? selectedRow + currentSelectedShipSize - 1 > 10
                                    : selectedCol + currentSelectedShipSize - 1 > 10;

                            boolean canPlace = GameStatus.canPlaceShip(
                                    currentSelectedShipSize, currentSelectedShipIsVertical, selectedCol - 1, selectedRow - 1);

                            if (outOfBounds || !canPlace) {
                                notificationText.setFill(Color.RED);
                                notificationText.setText(I18nLoader.getText("inGame.notification.shipPlacement.invalid"));
                            } else {
                                currentPlacedShipRow = selectedRow;
                                currentPlacedShipCol = selectedCol;
                                currentSelectedShipIsPlaced = true;
                            }
                        }
                    });
                }

                myBoard.add(cell, col, row);
            }
        }

        return myBoard;
    }

    @NotNull
    private GridPane setOpponentBoard() {
        GridPane opponentBoard = new GridPane();

        for (int col = 0; col <= 10; col++) {

            ColumnConstraints colConstraints = new ColumnConstraints(40);
            colConstraints.setFillWidth(true);
            colConstraints.setHgrow(Priority.NEVER);
            opponentBoard.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints(40);
            rowConstraints.setFillHeight(true);
            rowConstraints.setVgrow(Priority.NEVER);
            opponentBoard.getRowConstraints().add(rowConstraints);

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
                    int finalCol = col;
                    int finalRow = row;
                    cell.setOnMouseClicked(event -> {
                        hitPosX = finalCol - 1;
                        hitPosY = finalRow - 1;

                        if (lockedCells.contains(cell) || !GameStatus.getIsMyTurnValue() || !GameStatus.allShipsSet() || !GameStatus.allShipsSetOpponent()) {
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
        toSurrenderButton.setOnAction(event -> {
            Text confirmationText = new Text(I18nLoader.getText("inGame.surrender.confirm"));

            SimpleConfirmationPopup confirmationPopup = new SimpleConfirmationPopup();
            confirmationPopup.displayPopup(confirmationText, confirmed -> {
                if (confirmed) {
                    ApiService.surrender();
                    UserStatus.setInGameStatus(false);
                    sceneManager.showHomeScene(true);
                    UserOverlay.showOverlay();
                    GameStatus.setGameKey(null);
                    GameStatus.setOpponentUserName(null);
                }
            });
        });
        toSurrenderButton.getStyleClass().add("red-button");

        toMakeHit = new Button(I18nLoader.getText("inGame.makeHit"));
        toMakeHit.setOnAction(event -> {
            if (selectedCell != null & currentDot != null) {
                ApiService.hit(hitPosX, hitPosY);

                if (GameStatus.getCurrentHitType() == HitType.HIT) {
                    notificationText.setText(I18nLoader.getText("inGame.notification.hit"));
                    notificationText.setFill(Color.LIGHTGREEN);
                } else if (GameStatus.getCurrentHitType() == HitType.DESTROYED) {
                    notificationText.setText(I18nLoader.getText("inGame.notification.shipDestroyed"));
                    notificationText.setFill(Color.LIGHTGREEN);
                } else if (GameStatus.getCurrentHitType() == HitType.WON) {
                    notificationText.setText(I18nLoader.getText("inGame.notification.won"));
                    notificationText.setFill(Color.LIGHTGREEN);

                    PauseTransition notificationTextPause = new PauseTransition(Duration.seconds(3));
                    notificationTextPause.setOnFinished(actionEvent -> {
                        UserStatus.setInGameStatus(false);
                        sceneManager.showHomeScene(true);
                        UserOverlay.showOverlay();
                        GameStatus.setGameKey(null);
                        GameStatus.setOpponentUserName(null);
                    });

                    notificationTextPause.play();
                } else if (GameStatus.getCurrentHitType() == HitType.MISS) {
                    notificationText.setText(I18nLoader.getText("inGame.notification.noHit"));
                    notificationText.setFill(Color.YELLOW);

                    GameStatus.setIsMyTurn(false);
                } else {
                    GameStatus.setIsMyTurn(false);
                }

                currentDot.setFill(GameStatus.getCurrentHitType() == HitType.HIT || GameStatus.getCurrentHitType() == HitType.DESTROYED || GameStatus.getCurrentHitType() == HitType.WON ? Color.RED : Color.WHITE);
                lockedCells.add(selectedCell);

                selectedCell = null;
                currentDot = null;

                toMakeHit.setDisable(true);
            }
        });
        toMakeHit.getStyleClass().add("green-button");
        toMakeHit.setDisable(true);
        toMakeHit.visibleProperty().bind(GameStatus.getIsMyTurn());

        box.getChildren().addAll(toMakeHit, toSurrenderButton);

        return box;
    }

    public void displayMyTurn() {
        notificationText.setText(I18nLoader.getText("inGame.notification.myTurn"));
        notificationText.setFill(Color.WHITE);
    }

    public void handleLose() {
        notificationText.setText(I18nLoader.getText("inGame.notification.lose"));
        notificationText.setFill(Color.RED);

        PauseTransition notificationTextPause = new PauseTransition(Duration.seconds(3));
        notificationTextPause.setOnFinished(event -> {
            UserStatus.setInGameStatus(false);
            sceneManager.showHomeScene(true);
            UserOverlay.showOverlay();
            GameStatus.setGameKey(null);
            GameStatus.setOpponentUserName(null);
        });
        notificationTextPause.play();
    }

    public void handleSurrender() {
        notificationText.setText(I18nLoader.getText("inGame.notification.opponentSurrendered"));
        notificationText.setFill(Color.RED);

        PauseTransition notificationTextPause = new PauseTransition(Duration.seconds(3));
        notificationTextPause.setOnFinished(event -> {
            UserStatus.setInGameStatus(false);
            sceneManager.showHomeScene(true);
            UserOverlay.showOverlay();
            GameStatus.setGameKey(null);
            GameStatus.setOpponentUserName(null);
        });
        notificationTextPause.play();
    }

    private void setOpponentTurnNotification(boolean isMyTurn) {
        if (!isMyTurn) {
            notificationText.setFill(Color.RED);
            notificationText.setText(I18nLoader.getText("inGame.notification.opponentTurn"));
        }
    }

    @NotNull
    @Contract("_ -> param1")
    private HBox setShipPlaceholder(@NotNull HBox box) {
        box.setAlignment(Pos.CENTER);

        imageViewOfShipOfCurrentType = new ImageView();

        counterTextOfShipsOfCurrentType = new Text();
        counterTextOfShipsOfCurrentType.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));
        counterTextOfShipsOfCurrentType.setTextAlignment(TextAlignment.CENTER);
        counterTextOfShipsOfCurrentType.getStyleClass().add("board-text");

        Button toSetVerticalButton = new Button("H");
        toSetVerticalButton.getStyleClass().add("vertical-button");
        toSetVerticalButton.setOnAction(event -> {
            String oldText = toSetVerticalButton.getText();
            String newText = Objects.equals(oldText, "H") ? "V" : "H";
            currentSelectedShipIsVertical = Objects.equals(newText, "V");
            toSetVerticalButton.setText(newText);
        });

        box.getChildren().addAll(imageViewOfShipOfCurrentType, counterTextOfShipsOfCurrentType, toSetVerticalButton);

        return box;
    }

    private void placeShips(int size) {
        if (size > 5) {
            return;
        }

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
            currentSelectedShipSize = size;
            Image shipImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/ships/size_" + size + "_ship.png")).toExternalForm());
            imageViewOfShipOfCurrentType.setImage(shipImage);
            imageViewOfShipOfCurrentType.setFitWidth(40 * size);
            imageViewOfShipOfCurrentType.setFitHeight(40);

            ImageView shipImageInBoard = new ImageView(shipImage);
            shipImageInBoard.setFitWidth(40 * size);
            shipImageInBoard.setFitHeight(40);

            counterTextOfShipsOfCurrentType.setText(counter + "X");

            currentSelectedShipIsPlaced = false;

            // Poll every 100ms to check if the ship has been placed
            Timeline waitForPlacement = new Timeline();
            waitForPlacement.getKeyFrames().add(new KeyFrame(Duration.millis(100), event -> {
                if (currentSelectedShipIsPlaced) {
                    waitForPlacement.stop();
                    notificationText.setText("");
                    counterTextOfShipsOfCurrentType.setText(counter - 1 + "X");

                    int posY = currentSelectedShipIsVertical ? 1 : currentSelectedShipSize;
                    int posX = currentSelectedShipIsVertical ? currentSelectedShipSize : 1;

                    if (currentSelectedShipIsVertical) {
                        shipImageInBoard.setRotate(90);
                        shipImageInBoard.setFitWidth(40 * size);
                        shipImageInBoard.setFitHeight(40);
                        shipImageInBoard.setTranslateX((double) (-(40 * size - 40)) / 2);
                    }

                    myBoard.add(shipImageInBoard, currentPlacedShipCol, currentPlacedShipRow, posY, posX);

                    GameStatus.placeShip(currentSelectedShipSize, currentSelectedShipIsVertical, currentPlacedShipCol - 1, currentPlacedShipRow - 1);

                    placeShips(size);

                    if (GameStatus.allShipsSet()) {
                        ApiService.setupBoard(GameStatus.getShips());

                        notificationText.setFill(Color.LIGHTGREEN);
                        notificationText.setText(I18nLoader.getText("inGame.notification.shipPlacement.allShipsPlaced"));
                    }
                }
            }));
            waitForPlacement.setCycleCount(Animation.INDEFINITE);
            waitForPlacement.play();
        }
    }

    public void setCrossOnField(int posX, int posY) {
        Image crossImage = new Image(Objects.requireNonNull(getClass().getResource("/assets/textures/cross.png")).toExternalForm());
        ImageView crossImageInBoard = new ImageView(crossImage);
        crossImageInBoard.setFitWidth(40);
        crossImageInBoard.setFitHeight(40);

        myBoard.add(crossImageInBoard, posY, posX, 1, 1);
    }

    public Pane getRoot() {
        return root;
    }
}