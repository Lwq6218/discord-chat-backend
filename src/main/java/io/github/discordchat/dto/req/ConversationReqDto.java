package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @classname ConversationReqDto
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Data
public class ConversationReqDto {
    @Schema(description = "成员1ID")
    private Long memberOneId;

   @Schema(description = "成员2ID")
    private Long memberTwoId;
}
