package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @classname ChannelUpdateRoleReqDto
 * @description TODO
 * @date 2024/6/21
 * @created by lwq
 */
@Data
public class MemberUpdateRoleReqDto {

    @Schema(description = "角色")
    Integer role;
}
