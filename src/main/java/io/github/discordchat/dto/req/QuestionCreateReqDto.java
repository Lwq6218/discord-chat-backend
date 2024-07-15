package io.github.discordchat.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @classname QuestionCreateReqDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
public class QuestionCreateReqDto {
    @Schema(description = "问题标题")
    @Parameter(required = true)
    private String title;

    @Schema(description = "问题内容")
    @Parameter(required = true)
    private String content;

    @Schema(description = "问题标签名称")
    @Parameter(required = true)
    private List<String> tagsName;

}
