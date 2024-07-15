package io.github.discordchat.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @classname UserProfileDto
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Data
@Builder
public class UserProfileDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
}
