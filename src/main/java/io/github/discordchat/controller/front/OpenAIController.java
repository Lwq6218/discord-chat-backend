package io.github.discordchat.controller.front;

import com.gearwenxin.client.ChatClient;
import com.gearwenxin.entity.response.ChatResponse;
import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.service.OpenAIService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;


/**
 * @classname OpenAIController
 * @description TODO
 * @date 2024/6/29
 * @created by lwq
 */
@Tag(name = "OpenAIController", description = "AI模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_OPENAI_URL_PREFIX)
public class OpenAIController {
    @Resource
    // 与上方配置类中的 @Qualifier("Ernie") 保持一致
    @Qualifier("Ernie")
    private ChatClient chatClient;

    @Resource
    private OpenAIService openAIService;


    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> chatSingle(@RequestParam String msg) {
        Mono<ChatResponse> response = chatClient.chat(msg);
        return response.map(ChatResponse::getResult);
    }

    @GetMapping("generate")
    public Flux<String> generate(@PathParam("questionId") Long questionId) {
        return openAIService.generateAnswer(questionId, UserHolder.getUserId());
    }

    @GetMapping("token")
    public RestResp<String> getAccessToken() throws IOException {
        return openAIService.getAccessToken();
    }


}
