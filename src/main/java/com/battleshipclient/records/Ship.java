package com.battleshipclient.records;

public record Ship(ShipType type, boolean isVertical, int posX, int posY) {
}
