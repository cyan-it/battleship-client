package com.battleshipclient;

import com.battleshipclient.scenes.CreateGameScene;
import com.battleshipclient.scenes.PlayGameScene;
import com.battleshipclient.status.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WebSocketClientService {

    private static final String WS_URL = "ws://api.bs.sh00ckbass.de/battleship-broker";

    private final WebSocketStompClient stompClient;
    private StompSession stompSession;

    private CreateGameScene gameScene;
    private PlayGameScene playGameScene;

    public WebSocketClientService() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);

        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);

        stompClient.setMessageConverter(converter);

        stompClient.setDefaultHeartbeat(new long[]{10000, 10000});

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("stomp-heartbeat-thread-");
        taskScheduler.initialize();
        stompClient.setTaskScheduler(taskScheduler);
    }

    public void connect() {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        StompSessionHandler handler = new StompSessionHandler(this);
        headers.add("Authorization", "Bearer " + UserStatus.getAccessToken());

        CompletableFuture<StompSession> futureSession = stompClient.connectAsync(WS_URL, headers, handler);

        futureSession.orTimeout(10, TimeUnit.SECONDS)
                .thenAccept(session -> {
                    this.stompSession = session;
                    System.out.println("WebSocket connection established: " + session.getSessionId());
                })
                .exceptionally(ex -> {
                    System.err.println("WebSocket connection failed: " + ex.getMessage());
                    ex.printStackTrace();
                    return null;
                });
    }

    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            System.out.println("WebSocket disconnected.");
        }
    }


    public PlayGameScene getPlayGameScene() {
        return playGameScene;
    }

    public void setPlayGameScene(PlayGameScene playGameScene) {
        this.playGameScene = playGameScene;
    }

    public CreateGameScene getGameScene() {
        return gameScene;
    }

    public void setGameScene(CreateGameScene gameScene) {
        this.gameScene = gameScene;
    }
}

