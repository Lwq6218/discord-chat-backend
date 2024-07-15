package io.github.discordchat.manager.cache;

import io.github.discordchat.core.constant.CacheConst;
import io.github.discordchat.dao.entity.UserProfile;
import io.github.discordchat.dao.mapper.UserProfileMapper;
import io.github.discordchat.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @classname UserProfileCacheManager
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Component
@RequiredArgsConstructor
public class UserProfileCacheManager {

    private final UserProfileMapper userProfileMapper;

    /**
     * 查询用户信息，并放入缓存中
     * Cacheable注解： value:用来指定袋存组件的名字 cacheManager:可以用来指定缓存管理器，从那个缓存管理器里面获取缓存。
     */
    @Cacheable(cacheManager = CacheConst.REDIS_CACHE_MANAGER,
            value = CacheConst.USER_PROFILE_CACHE_NAME)
    public UserProfileDto getUser(Long userId) {
        UserProfile userProfile = userProfileMapper.selectById(userId);
        if (Objects.isNull(userProfile)) {
            return null;
        }
        return UserProfileDto.builder().id(userProfile.getId()).build();
    }
}
