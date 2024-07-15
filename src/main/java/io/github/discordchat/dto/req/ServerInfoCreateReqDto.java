package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @classname ServerCreateReqDto
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */

@Data
public class ServerInfoCreateReqDto {
    @Schema(description = "服务器名称")
    @Parameter(required = true)
    @NotBlank(message = "服务器名称不能为空!")
    private String name;

    @Schema(description = "服务器图片地址")
    @Parameter(required = true)
    @NotBlank(message = "服务器图片地址不能为空!")
    private String imageUrl;


}
