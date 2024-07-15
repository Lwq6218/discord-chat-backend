package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname ChannelInfoRespDto
 * @description TODO
 * @date 2024/6/18
 * @created by lwq
 */
@Data
@Builder
public class ChannelInfoRespDto {
    @Schema(description = "频道id")
    private Long id;

    @Schema(description = "频道名称")
    private String name;

    @Schema(description = "频道描述")
    private Long serverId;

    @Schema(description = "频道类型 0:文字 1:语音 2:视频")
    private Integer type;
}
