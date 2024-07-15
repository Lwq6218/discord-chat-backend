package io.github.discordchat.dto.req;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @classname UserPofileReqDto
 * @description TODO
 * @date 2024/7/1
 * @created by lwq
 */
@Data
public class UserProfileReqDto {
     /**
     * 用户名称
     */
    private String name;

    /**
     * 用户昵称
     */
    private String nickName;


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


}
