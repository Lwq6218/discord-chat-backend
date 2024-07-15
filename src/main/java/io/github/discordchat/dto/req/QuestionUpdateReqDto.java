package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @classname QuestionUpdateReqDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
public class QuestionUpdateReqDto {
    @Schema(description = "问题ID")
    @Parameter(required = true)
    private Long id;

    @Schema(description = "问题标题")
    @Parameter(required = true)
    private String title;

    @Schema(description = "问题内容")
    @Parameter(required = true)
    private String content;

}
