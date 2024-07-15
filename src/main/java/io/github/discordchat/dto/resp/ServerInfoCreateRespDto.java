package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname ServerCreateRespDto
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
@Data
@Builder
public class ServerInfoCreateRespDto {
    @Schema(description = "服务器ID")
    private Long id;

    @Schema(description = "服务器名称")
    private String name;

}
