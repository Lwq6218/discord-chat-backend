package io.github.discordchat.core.config;

import io.github.discordchat.core.constant.CacheConst;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @classname CacheConfig
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Configuration
public class CacheConfig {
      /**
     * Redis 缓存管理器
     */
    @Bean(CacheConst.REDIS_CACHE_MANAGER)
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        //RedisCacheWriter.nonLockingRedisCacheWriter()：创建一个非锁定的 RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(
            connectionFactory);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues().prefixCacheNameWith(CacheConst.REDIS_CACHE_PREFIX);

        Map<String, RedisCacheConfiguration> cacheMap = new LinkedHashMap<>(
            CacheConst.CacheEnum.values().length);
        // 类型推断 var 非常适合 for 循环，JDK 10 引入，JDK 11 改进
        for (var c : CacheConst.CacheEnum.values()) {
            if (c.isRemote()) {
                if (c.getTtl() > 0) {
                    cacheMap.put(c.getName(),
                        RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
                            .prefixCacheNameWith(CacheConst.REDIS_CACHE_PREFIX)
                            .entryTtl(Duration.ofSeconds(c.getTtl())));
                } else {
                    cacheMap.put(c.getName(),
                        RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
                            .prefixCacheNameWith(CacheConst.REDIS_CACHE_PREFIX));
                }
            }
        }

        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter,
            defaultCacheConfig, cacheMap);
        redisCacheManager.setTransactionAware(true);
        redisCacheManager.initializeCaches();
        return redisCacheManager;
    }
}
