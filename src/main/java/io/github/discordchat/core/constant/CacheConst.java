package io.github.discordchat.core.constant;

/**
 * @classname CacheConst
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
public class CacheConst {
    /**
     * 本项目 Redis 缓存前缀
     */
    public static final String REDIS_CACHE_PREFIX = "Cache::DiscordChat::";



    /**
     * Redis 缓存管理器
     */
    public static final String REDIS_CACHE_MANAGER = "redisCacheManager";


    /**
     * 图片验证码缓存 KEY
     */
    public static final String IMG_VERIFY_CODE_CACHE_KEY =
            REDIS_CACHE_PREFIX + "imgVerifyCodeCache::";

    /**
     * 用户信息缓存
     */
    public static final String USER_PROFILE_CACHE_NAME = "userInfoCache";


    /**
     * 缓存配置常量
     */
    public enum CacheEnum {


        USER_Profile_CACHE(2, USER_PROFILE_CACHE_NAME, 60 * 60 * 24, 10000);


        /**
         * 缓存类型 0-本地 1-本地和远程 2-远程
         */
        private final int type;
        /**
         * 缓存的名字
         */
        private final String name;
        /**
         * 失效时间（秒） 0-永不失效
         */
        private final int ttl;
        /**
         * 最大容量
         */
        private final int maxSize;

        CacheEnum(int type, String name, int ttl, int maxSize) {
            this.type = type;
            this.name = name;
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        public boolean isLocal() {
            return type <= 1;
        }

        public boolean isRemote() {
            return type >= 1;
        }

        public String getName() {
            return name;
        }

        public int getTtl() {
            return ttl;
        }

        public int getMaxSize() {
            return maxSize;
        }

    }
}
