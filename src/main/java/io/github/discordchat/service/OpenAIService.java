package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface OpenAIService {
    Flux<String> generateAnswer(Long questionId, Long userId);

    RestResp<String> getAccessToken() throws IOException;

}
