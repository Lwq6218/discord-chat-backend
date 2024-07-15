package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname ServerInfoRespDto
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */
@Data
@Builder
public class ServerInfoRespDto {
    @Schema(description = "服务器id")
    private Long id;
    @Schema(description = "服务器名称")
    private String name;
    @Schema(description = "服务器拥有者ID")
    private Long profileId;
    @Schema(description = "服务器图标")
    private String imageUrl;
    @Schema(description = "服务器邀请码")
    private String inviteCode;
}
