package io.github.discordchat.dto.req;

import lombok.Builder;
import lombok.Data;

/**
 * @classname questionVoteReqDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
public class ActionReqDto {

    private Long questionId;

    private Long answerId;

    private  String type;

    private boolean hasUpVoted;

    private boolean hasSaved;

    private boolean hasDownVoted;
}
