package io.github.discordchat.dto.resp;

import io.github.discordchat.dao.entity.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @classname MemberInfoWithProfile
 * @description TODO
 * @date 2024/6/19
 * @created by lwq
 */
@Data
@Builder
public class MemberInfoWithProfileRespDto {
    @Schema(description = "成员id")
    private Long id;

    @Schema(description = "成员名称")
    private Integer role;

    @Schema(description = "成员简历ID")
    private Long profileId;

    @Schema(description = "成员简历")
    private UserProfile profile;

}
