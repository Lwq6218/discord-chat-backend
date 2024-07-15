package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @classname MessageReqDto
 * @description TODO
 * @date 2024/6/20
 * @created by lwq
 */

@Data
public class MessageReqDto {
    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "是否为文件 0:否 1:是")
    private String isFile = "0";
}
