package io.github.discordchat.controller.front;

import io.github.discordchat.core.auth.UserHolder;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.core.config.WebSocketHandler;
import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.dto.req.MessageReqDto;
import io.github.discordchat.dto.resp.MessageRespDto;
import io.github.discordchat.dto.resp.MessagesWithMemberWithProfileRespDto;
import io.github.discordchat.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @classname MessageController
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */

@Tag(name = "MessageController", description = "消息模块")
@SecurityRequirement(name = SystemConfigConst.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConst.API_MESSAGE_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    private final WebSocketHandler webSocketHandler;

    @GetMapping("list")
    public RestResp<MessagesWithMemberWithProfileRespDto> listMessage(@RequestParam("channelId") Long channelId, @RequestParam(value = "cursor", required = false) Long cursor) {
        return messageService.listMessage(channelId, cursor);
    }

    @PostMapping("socket")
    public RestResp<Void> sendMessage(@RequestParam("serverId") Long serverId, @RequestParam("channelId") Long channelId, @Valid @RequestBody MessageReqDto dto) {
        //插入消息到数据库
        MessageRespDto messageRespDto = messageService.create(serverId, channelId, dto, UserHolder.getUserId());
        //构建广播的key
        String key = String.format("channel:%s:messages", channelId);
        //广播消息
        try {
            webSocketHandler.broadcastToChannel(key, messageRespDto);
            log.info("广播消息成功");
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
        return RestResp.ok();
    }
    @DeleteMapping("/socket/{id}")
    public RestResp<Void> deleteMessage(@PathVariable("id") Long id, @PathParam("channelId") Long channelId,@RequestParam("serverId") Long serverId){
        //逻辑删除消息
        MessageRespDto messageRespDto = messageService.deleteMessage(UserHolder.getUserId(), id, channelId, serverId);
       //构建广播的key
        String key = String.format("channel:%s:messages:update", channelId);
          //广播消息
        try {
            webSocketHandler.broadcastToChannel(key, messageRespDto);
            log.info("广播消息成功");
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
        return RestResp.ok();
    }
      @PatchMapping("/socket/{id}")
    public RestResp<Void> updateMessage(@PathVariable("id") Long id, @PathParam("channelId") Long channelId,@RequestParam("serverId") Long serverId ,@Valid @RequestBody MessageReqDto dto){
        //更新消息
        MessageRespDto messageRespDto = messageService.updateMessage(UserHolder.getUserId(), id, channelId, serverId,dto.getContent());
       //构建广播的key
        String key = String.format("channel:%s:messages:update", channelId);
          //广播消息
        try {
            webSocketHandler.broadcastToChannel(key, messageRespDto);
            log.info("广播消息成功");
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
        return RestResp.ok();
    }
}
