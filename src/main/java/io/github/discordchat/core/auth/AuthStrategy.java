package io.github.discordchat.core.auth;

import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.constant.SystemConfigConst;
import io.github.discordchat.core.util.JwtUtil;
import io.github.discordchat.dto.UserProfileDto;
import io.github.discordchat.manager.cache.UserProfileCacheManager;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface AuthStrategy {
     /**
     * 用户认证授权
     *
     * @param token      登录 token
     * @param requestUri 请求的 URI
     * @throws BusinessException 认证失败则抛出业务异常
     */
    void auth(String token, String requestUri) throws BusinessException;

    /**
     * 前台多系统单点登录统一账号认证授权（及后面可能会扩展的后台系统等）
     *
     * @param jwtUtils             jwt 工具
     * @param userProfileCacheManager 用户缓存管理对象
     * @param token                token 登录 token
     * @return 用户ID
     */
    default Long authSSO(JwtUtil jwtUtils,  UserProfileCacheManager userProfileCacheManager,
                         String token) {

        if (!StringUtils.hasText(token)) {
            // token 为空
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        Long userId = jwtUtils.parseToken(token, SystemConfigConst.DISCORDCHAT_FRONT_KEY);
        if (Objects.isNull(userId)) {
            // token 解析失败
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        UserProfileDto userProfile = userProfileCacheManager.getUser(userId);
        if (Objects.isNull(userProfile)) {
            // 用户不存在
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        // 设置 userId 到当前线程
        UserHolder.setUserId(userId);
        // 返回 userId
        return userId;
    }
}
