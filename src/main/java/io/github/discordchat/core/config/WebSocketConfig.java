package io.github.discordchat.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @classname WebSocketConfig
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
 private final WebSocketHandler webSocketHandler;

 public WebSocketConfig(WebSocketHandler webSocketHandler) {
    this.webSocketHandler = webSocketHandler;
 }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
       registry.addHandler(webSocketHandler, "/ws")
               .setAllowedOrigins("*","file://");
    }
}
