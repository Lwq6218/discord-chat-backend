package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.ConversationReqDto;
import io.github.discordchat.dto.resp.ConversationRespDto;
import io.github.discordchat.service.ConversationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @classname ConversationController
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */

@Tag(name = "ChannelController", description = "交流模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_CONVERSATION_URL_PREFIX)
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService  conversationService;

   @PostMapping
    public RestResp<ConversationRespDto> getConversation(@Valid @RequestBody ConversationReqDto dto){
 return    conversationService.getConversation(dto.getMemberOneId(),dto.getMemberTwoId());
    }
}
