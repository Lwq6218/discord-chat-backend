package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.config.WebSocketHandler;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.DirectMessageReqDto;
import io.github.discordchat.dto.req.MessageReqDto;
import io.github.discordchat.dto.resp.DirectMessageRespDto;
import io.github.discordchat.dto.resp.DirectMessagesWithMemberWithProfileRespDto;
import io.github.discordchat.service.DirectMessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @classname DirectMessageController
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */

@Tag(name = "DirectMessageController", description = "直接消息模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_DIRECT_MESSAGE_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class DirectMessageController {

private final DirectMessageService directMessageService;

private final WebSocketHandler webSocketHandler;
    @GetMapping("list")
    public RestResp<DirectMessagesWithMemberWithProfileRespDto> listDirectMessage(@RequestParam("conversationId") Long conversationId, @RequestParam(value = "cursor", required = false) Long cursor) {
        return directMessageService.listDirectMessage(conversationId, cursor);
    }



    @PostMapping("socket")
    public RestResp<Void> sendMessage(@RequestParam("conversationId") Long conversationId, @Valid @RequestBody DirectMessageReqDto dto) {
        //插入消息到数据库
        DirectMessageRespDto directMessageRespDto = directMessageService.create( conversationId, dto.getContent(), UserHolder.getUserId());
        //构建广播的key
        String key = String.format("conversation:%s:messages", conversationId);
        //广播消息
        try {
            webSocketHandler.broadcastToChannel(key, directMessageRespDto);
            log.info("广播消息成功");
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
        return RestResp.ok();
    }
    @DeleteMapping("/socket/{id}")
    public RestResp<Void> deleteMessage(@PathVariable("id") Long id, @PathParam("conversationId") Long conversationId){
        //逻辑删除消息
         DirectMessageRespDto directMessageRespDto = directMessageService.deleteMessage(UserHolder.getUserId(), id, conversationId);
        //构建广播的key
        String key = String.format("conversation:%s:messages:update", conversationId);
        //广播消息
        try {
            webSocketHandler.broadcastToChannel(key, directMessageRespDto);
            log.info("广播消息成功");
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
        return RestResp.ok();
    }
    @PatchMapping("/socket/{id}")
    public RestResp<Void> updateMessage(@PathVariable("id") Long id, @PathParam("conversationId") Long conversationId ,@Valid @RequestBody MessageReqDto dto){
        //更新消息
        DirectMessageRespDto directMessageRespDto = directMessageService.updateMessage(UserHolder.getUserId(), id, conversationId,dto.getContent());
        //构建广播的key
        String key = String.format("conversation:%s:messages:update", conversationId);
        //广播消息
        try {
            webSocketHandler.broadcastToChannel(key, directMessageRespDto);
            log.info("广播消息成功");
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
        return RestResp.ok();
    }
}
