package io.github.discordchat.dto.resp;

import io.github.discordchat.dao.entity.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @classname AnswerWithProfileRespDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
@Builder
public class AnswerWithProfileRespDto {
    @Schema(description = "回答id")
    private Long id;

    @Schema(description = "回答内容")
    private String content;

    @Schema(description = "回答点赞数")
    private Long upvotes;

    @Schema(description = "回答踩数")
    private Long downvotes;

    @Schema(description = "问题id")

    private Long questionId;
    @Schema(description = "是否已经点赞")

    private boolean hasUpVoted;
    @Schema(description = "是否已经踩")
    private boolean hasDownVoted;

    @Schema(description = "作者")
    private UserProfile author;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
