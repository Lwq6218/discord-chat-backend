package io.github.discordchat.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.jsoup.internal.FieldsAreNonnullByDefault;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @classname WebSocketHandler
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
     private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper ;
    private WebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        startHeartBeat(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }
    @Override
    public void handleTransportError(@NotNull WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
    }
    public void broadcastToChannel(String key, Object message) throws IOException {
        String payload = objectMapper.writeValueAsString(new BroadcastMessage(key, message));
        for (WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(payload));
        }
    }

    private static class BroadcastMessage {
        private String key;
        private Object message;

        public BroadcastMessage(@JsonProperty("key") String key, @JsonProperty("message") Object message) {
            this.key = key;
            this.message = message;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }
    }
     private void startHeartBeat(WebSocketSession session) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage("{\"type\":\"PING\"}"));
                } else {
                    scheduler.shutdown();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 30, 30, TimeUnit.SECONDS); // Send a heartbeat every 30 seconds
    }
}
