package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname UserLoginRespDto
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Data
@Builder
public class UserLoginRespDto {
   @Schema(description = "用户ID")
    private Long uid;

    @Schema(description = "用户token")
    private String token;
}
