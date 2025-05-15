package com.battleshipclient;

import com.battleshipclient.records.Notification;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class StompSessionHandler extends StompSessionHandlerAdapter {

    private final String destination = "/user/queue/notification";

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Notification.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Notification notification = (Notification) payload;

                switch (notification.type()) {
                    case PLAYER_JOINED_GAME ->
                    case GAME_READY ->
                    case HIT ->
                    case SHIP_DESTROYED ->
                    case OPPONENT_SURRENDERED ->
                    case GAME_FINISHED ->
                }
            }
        });
    }
}
