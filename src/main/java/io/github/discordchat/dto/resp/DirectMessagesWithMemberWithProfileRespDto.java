package io.github.discordchat.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @classname DirectMessagesWithMemberWithProfileRespDto
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Data
@Builder
public class DirectMessagesWithMemberWithProfileRespDto {
     private List<DirectMessageRespDto> items;
    private Long nextCursor;
}
