package io.github.discordchat.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @classname MessagesWithMemberWithProfileRespDto
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */
@Data
@Builder
public class MessagesWithMemberWithProfileRespDto {

    private List<MessageRespDto> items;
    private Long nextCursor;
}
