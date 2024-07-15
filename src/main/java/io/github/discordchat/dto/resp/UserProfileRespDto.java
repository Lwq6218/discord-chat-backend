package io.github.discordchat.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @classname UserProfileRespDto
 * @description TODO
 * @date 2024/6/28
 * @created by lwq
 */
@Data
@Builder
public class UserProfileRespDto {
    private Long id;

    /**
     * 用户名称
     */
    private String name;


    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户性别； 0-男，1-女
     */
    private Integer userSex;

    /**
     * 用户头像
     */
    private String imageUrl;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
