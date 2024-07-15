package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.MessageReqDto;
import io.github.discordchat.dto.resp.MessageRespDto;
import io.github.discordchat.dto.resp.MessagesWithMemberWithProfileRespDto;

import java.util.List;

/**
 * @classname MessageService
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */
public interface MessageService {
    RestResp<MessagesWithMemberWithProfileRespDto> listMessage(Long channelId, Long cursor);

    MessageRespDto create(Long serverId, Long channelId, MessageReqDto dto, Long userId);

    MessageRespDto deleteMessage(Long userId, Long id, Long channelId, Long serverId);

    MessageRespDto updateMessage(Long userId, Long id, Long channelId, Long serverId, String content);
}
