package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname ImageVerifyCodeRespDto
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Data
@Builder
public class ImageVerifyCodeRespDto {
    /**
     * 当前会话ID，用于标识改图形验证码属于哪个会话
     */
    @Schema(description = "sessionId")
    private String sessionId;

    /**
     * Base64 编码的验证码图片
     */
    @Schema(description = "Base64 编码的验证码图片")
    private String img;
}
