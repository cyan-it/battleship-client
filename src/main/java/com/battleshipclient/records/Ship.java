package com.battleshipclient.records;

import com.battleshipclient.enums.ShipType;

public record Ship(ShipType type, boolean isVertical, int posX, int posY) {
}
