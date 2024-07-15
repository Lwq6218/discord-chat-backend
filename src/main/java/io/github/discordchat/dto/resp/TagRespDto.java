package io.github.discordchat.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @classname TagRespDto
 * @description TODO
 * @date 2024/6/27
 * @created by lwq
 */
@Data
@Builder
public class TagRespDto {
    private Long id;

    private String name;

    private Long questionCount;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
