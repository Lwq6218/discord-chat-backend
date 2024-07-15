package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @classname UserRegisterReqDto
 * @description 用户注册DTO
 * @date 2024/6/13
 * @created by lwq
 */
@Data
public class UserRegisterReqDto {
    @Schema(description = "用户名")
    @Parameter(required = true)
    @NotBlank(message = "用户名不能为空!")
    private String name;

    @Schema(description = "密码")
    @Parameter(required = true)
    @NotBlank(message = "密码不能为空!")
    private String password;

    @Schema(description = "验证码")
    @Parameter(required = true)
    @NotBlank(message = "验证码不能为空!")
    @Pattern(regexp = "[0-9]{4}", message = "验证码格式不正确!")
    private String verifyCode;

    @Schema(description = "sessionId")
    @Parameter(required = true)
    @NotBlank
    @Length(min = 32, max = 32)
    private String sessionId;
}
