package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @classname UserLoginReqDto
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Data
public class UserLoginReqDto {
    @Schema(description = "用户名称")
    @Parameter(required = true)
    @NotBlank(message = "用户名称不能为空！")
    private String name;

    @Schema(description = "密码")
    @Parameter(required = true)
    @NotBlank(message = "密码不能为空！")
    private String password;
}
