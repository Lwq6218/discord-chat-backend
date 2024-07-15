package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname ConversationRespDto
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Data
@Builder
public class ConversationRespDto {
    @Schema(description = "会话ID")
    private Long id;

    @Schema(description = "成员1")
    private MemberInfoWithProfileRespDto memberOne;

    @Schema(description = "成员2")
    private MemberInfoWithProfileRespDto memberTwo;
}
