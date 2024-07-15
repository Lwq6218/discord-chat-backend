package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @classname DirectMessageReqDto
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Data
public class DirectMessageReqDto {

    @Schema(description = "消息内容")
    private String content;
}
