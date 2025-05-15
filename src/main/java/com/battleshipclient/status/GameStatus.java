package com.battleshipclient.status;

import com.battleshipclient.enums.HitType;
import com.battleshipclient.enums.ShipType;
import com.battleshipclient.records.Ship;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public class GameStatus {

    private static final BooleanProperty isMyTurn = new SimpleBooleanProperty();
    private static final List<Ship> ships = new ArrayList<>();
    private static boolean gameStarted = false;
    private static String gameKey;
    private static String opponentUserName;
    private static boolean allShipsSet = false;
    private static boolean allShipsSetOpponent = false;
    private static HitType currentHitType;

    private static int numberOfSize2ShipsToBeSet;
    private static int numberOfSize3ShipsToBeSet;
    private static int numberOfSize4ShipsToBeSet;
    private static int numberOfSize5ShipsToBeSet;

    public static HitType getCurrentHitType() {
        return currentHitType;
    }

    public static void setCurrentHitType(HitType type) {
        currentHitType = type;
    }

    public static String getOpponentUserName() {
        return opponentUserName;
    }

    public static void setOpponentUserName(String name) {
        opponentUserName = name;
    }

    public static String getGameKey() {
        return gameKey;
    }

    public static void setGameKey(String key) {
        gameKey = key;
    }

    public static boolean allShipsSetOpponent() {
        return allShipsSetOpponent;
    }

    public static void setAllShipsSetOpponent() { allShipsSetOpponent = true; }

    public static void startGame(boolean gameCreated) {
        gameStarted = true;
        isMyTurn.set(gameCreated);
        numberOfSize2ShipsToBeSet = 4;
        numberOfSize3ShipsToBeSet = 3;
        numberOfSize4ShipsToBeSet = 2;
        numberOfSize5ShipsToBeSet = 1;
        ships.clear();
    }

    public static boolean allShipsSet() {
        return allShipsSet;
    }

    public static void setOpponentTurn(boolean isOpponentTurn) {
        isMyTurn.set(isOpponentTurn);
    }

    public static ObservableValue<? extends Boolean> getIsMyTurn() {
        return isMyTurn;
    }

    public static boolean getIsMyTurnValue() {
        return isMyTurn.get();
    }

    public static int getNumberOfSize2ShipsToBeSet() {
        return numberOfSize2ShipsToBeSet;
    }

    public static int getNumberOfSize3ShipsToBeSet() {
        return numberOfSize3ShipsToBeSet;
    }

    public static int getNumberOfSize4ShipsToBeSet() {
        return numberOfSize4ShipsToBeSet;
    }

    public static int getNumberOfSize5ShipsToBeSet() {
        return numberOfSize5ShipsToBeSet;
    }

    public static List<Ship> getShips() {
        return ships;
    }

    public static void placeShip(int shipSize, boolean isVertical, int posX, int posY) {
        if (!gameStarted || allShipsSet) {
            return;
        }

        switch (shipSize) {
            case 2:
                numberOfSize2ShipsToBeSet--;
                ships.add(new Ship(ShipType.SMALL, isVertical, posX, posY));
                break;
            case 3:
                numberOfSize3ShipsToBeSet--;
                ships.add(new Ship(ShipType.MEDIUM, isVertical, posX, posY));
                break;
            case 4:
                numberOfSize4ShipsToBeSet--;
                ships.add(new Ship(ShipType.LARGE, isVertical, posX, posY));
                break;
            case 5:
                numberOfSize5ShipsToBeSet--;
                ships.add(new Ship(ShipType.XLARGE, isVertical, posX, posY));
                break;
            default:
                return;
        }
        allShipsSet = allShipsPlaced();
    }

    private static boolean allShipsPlaced() {
        return numberOfSize2ShipsToBeSet == 0 &&
                numberOfSize3ShipsToBeSet == 0 &&
                numberOfSize4ShipsToBeSet == 0 &&
                numberOfSize5ShipsToBeSet == 0;
    }

    public static boolean canPlaceShip(int shipSize, boolean isVertical, int posX, int posY) {
        for (Ship placedShip : ships) {
            int placedSize = switch (placedShip.type()) {
                case SMALL -> 2;
                case MEDIUM -> 3;
                case LARGE -> 4;
                case XLARGE -> 5;
            };

            for (int i = 0; i < shipSize; i++) {
                int newX = posX + (isVertical ? 0 : i);
                int newY = posY + (isVertical ? i : 0);

                for (int j = 0; j < placedSize; j++) {
                    int placedX = placedShip.posX() + (placedShip.isVertical() ? 0 : j);
                    int placedY = placedShip.posY() + (placedShip.isVertical() ? j : 0);

                    if (newX == placedX && newY == placedY) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
