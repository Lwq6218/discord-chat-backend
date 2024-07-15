package io.github.discordchat.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @classname DirectMessageRespDto
 * @description TODO
 * @date 2024/6/23
 * @created by lwq
 */
@Data
@Builder
public class DirectMessageRespDto {

    @Schema(description = "消息ID")
    private Long id;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "会话ID")
    private Long conversationId;

    @Schema(description = "成员ID")
    private Long memberId;

    @Schema(description = "文件地址")
    private String fileUrl;

    @Schema(description = "是否删除 0:未删除 1:已删除")
    private Integer deleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "成员信息")
    public MemberInfoWithProfileRespDto member;
}
