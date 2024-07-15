package io.github.discordchat.dto.resp;

import io.github.discordchat.dao.entity.Tag;
import io.github.discordchat.dao.entity.UserProfile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @classname QuestionWithTagsWithAnswersRespDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
@Builder
public class QuestionWithTagsRespDto {
    private Long id;

    private String title;

    private String content;

    private Long upvotes;

    private Long downvotes;

   private Long answerCount;

    private UserProfile author;

    private List<Tag> tags;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
