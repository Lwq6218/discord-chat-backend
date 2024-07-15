package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @classname ChannelCreateReqDto
 * @description TODO
 * @date 2024/6/19
 * @created by lwq
 */
@Data
public class ChannelCreateReqDto {

    @Schema(description = "频道名称")
    @Parameter(required = true)
    private String name;

    @Schema(description = "频道类型 0:TEXT 1:AUDIO 2:VIDEO")
    @Parameter(required = true)
    private Integer type;

}
