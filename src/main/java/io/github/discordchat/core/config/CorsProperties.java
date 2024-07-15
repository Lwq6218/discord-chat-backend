package io.github.discordchat.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @classname CorsProperties
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@ConfigurationProperties(prefix = "discordchat.cors")
public record CorsProperties(List<String> allowOrigins) {
}
