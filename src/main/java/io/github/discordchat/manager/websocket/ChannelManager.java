package io.github.discordchat.manager.websocket;

import jakarta.websocket.Session;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @classname ChannelManager
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */
public class ChannelManager {
    private static final Map<String, Set<Session>> channels = new ConcurrentHashMap<>();

      public static void broadcastToChannel(String channelId, String message) {
        channels.getOrDefault(channelId, Collections.emptySet())
                .forEach(session -> session.getAsyncRemote().sendText(message));
    }
}
