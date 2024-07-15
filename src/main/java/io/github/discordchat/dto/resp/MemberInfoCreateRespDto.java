package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname MemberCreateRespDto
 * @description TODO
 * @date 2024/6/17
 * @created by lwq
 */

@Data
@Builder
public class MemberInfoCreateRespDto {

    @Schema(description = "成员ID")
    private Long id;

}
