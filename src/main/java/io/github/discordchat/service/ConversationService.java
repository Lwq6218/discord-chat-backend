package io.github.discordchat.service;

import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.resp.ConversationRespDto;

public interface ConversationService {
    RestResp<ConversationRespDto> getConversation(Long memberOneId, Long memberTwoId);
}
