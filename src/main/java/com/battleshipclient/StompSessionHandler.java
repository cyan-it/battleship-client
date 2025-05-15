package com.battleshipclient;

import com.battleshipclient.records.HitNotification;
import com.battleshipclient.records.Notification;
import com.battleshipclient.status.GameStatus;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class StompSessionHandler extends StompSessionHandlerAdapter {

    private final WebSocketClientService webSocketClientService;

    public StompSessionHandler(WebSocketClientService webSocketClientService) {
        this.webSocketClientService = webSocketClientService;
    }

    @Override
    public void afterConnected(StompSession session, @NotNull StompHeaders connectedHeaders) {
        String destination = "/user/queue/notification";

        System.out.println("Subscribing to " + destination);

        session.subscribe(destination, new StompFrameHandler() {
            @NotNull
            @Override
            public Type getPayloadType(@NotNull StompHeaders headers) {
                return Notification.class;
            }

            @Override
            public void handleFrame(@NotNull StompHeaders headers, Object payload) {
                Notification notification = (Notification) payload;

                System.out.println(payload);

                switch (notification.type()) {
                    case PLAYER_JOINED_GAME:
                        String opponentName = (String) notification.data();

                        Platform.runLater(() -> webSocketClientService.getGameScene().afterPlayerJoined(opponentName));
                        break;
                    case GAME_READY:
                        GameStatus.setAllShipsSetOpponent();
                        break;
                    case GAME_FINISHED:
                        webSocketClientService.getPlayGameScene().handleLose();
                        break;
                    case OPPONENT_SURRENDERED:
                        webSocketClientService.getPlayGameScene().handleSurrender();
                        break;
                    case HIT, SHIP_DESTROYED:
                        HitNotification hitNotification = (HitNotification) notification.data();

                        webSocketClientService.getPlayGameScene().setCrossOnField(hitNotification.x(), hitNotification.y());
                        break;
                    case YOUR_TURN:
                        GameStatus.setIsMyTurn(true);
                        break;
                }
            }
        });
    }
}
