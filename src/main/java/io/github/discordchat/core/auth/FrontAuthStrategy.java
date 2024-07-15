package io.github.discordchat.core.auth;

import io.github.discordchat.core.common.exception.BusinessException;
import io.github.discordchat.core.util.JwtUtil;
import io.github.discordchat.manager.cache.UserProfileCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @classname FrontAuthStrategy
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Component
@RequiredArgsConstructor
public class FrontAuthStrategy implements AuthStrategy{
    private  final JwtUtil jwtUtil;
    private final UserProfileCacheManager userProfileCacheManager;

    @Override
    public void auth(String token, String requestUri) throws BusinessException {
       authSSO(jwtUtil, userProfileCacheManager, token);
    }
}
