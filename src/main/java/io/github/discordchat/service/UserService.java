package io.github.discordchat.service;

import io.github.discordchat.core.common.req.PageReqDto;
import io.github.discordchat.core.common.resp.PageRespDto;
import io.github.discordchat.core.common.resp.RestResp;
import io.github.discordchat.dto.req.UserLoginReqDto;
import io.github.discordchat.dto.req.UserProfileReqDto;
import io.github.discordchat.dto.req.UserRegisterReqDto;
import io.github.discordchat.dto.resp.UserLoginRespDto;
import io.github.discordchat.dto.resp.UserProfileRespDto;
import io.github.discordchat.dto.resp.UserRegisterRespDto;

/**
 * @classname UserService
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
public interface UserService {
    /**
     * 用户注册
     *
     * @param dto 注册参数
     * @return JWT
     */
    RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto);

    /**
     * 用户登录
     *
     * @param dto 登录参数
     * @return JWT + 昵称
     */
    RestResp<UserLoginRespDto> login(UserLoginReqDto dto);

    RestResp<PageRespDto<UserProfileRespDto>> listUsers(PageReqDto dto);

    RestResp<UserProfileRespDto> getUserInfo(Long userId);

    RestResp<Void> updateUserInfo(Long id, UserProfileReqDto dto);
}
