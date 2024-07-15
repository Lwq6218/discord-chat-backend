package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname MemberInfoRespDto
 * @description TODO
 * @date 2024/6/18
 * @created by lwq
 */
@Data
@Builder
public class MemberInfoRespDto {
    @Schema(description = "成员id")
    private Long id;

    @Schema(description = "成员名称")
    private String name;
    @Schema(description = "成员信息ID")
    private Long profileId;
    @Schema(description = "成员角色 0:管理员 1:版主 2:客人")
    private Integer role;


}
