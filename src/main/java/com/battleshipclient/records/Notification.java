package com.battleshipclient.records;

import com.battleshipclient.enums.NotificationType;

public record Notification(NotificationType type, Object data) {
}
