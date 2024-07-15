package io.github.discordchat.dto.req;

import lombok.Data;

/**
 * @classname AnswerCreateReqDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
public class AnswerCreateReqDto {
    private String content;
    private Long questionId;
}
