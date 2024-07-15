package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.resp.DirectMessageRespDto;
import io.github.discordchat.dto.resp.DirectMessagesWithMemberWithProfileRespDto;
import io.github.discordchat.dto.resp.MessageRespDto;

/**
 * @classname DirectMessageService
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
public interface DirectMessageService {
    RestResp<DirectMessagesWithMemberWithProfileRespDto> listDirectMessage(Long conversationId, Long cursor);

    DirectMessageRespDto create( Long conversationId, String content, Long userId);

    DirectMessageRespDto deleteMessage(Long userId, Long id, Long conversationId);

    DirectMessageRespDto updateMessage(Long userId, Long id, Long conversationId, String content);
}
