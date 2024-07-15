package io.github.discordchat.core.auth;

import lombok.experimental.UtilityClass;

/**
 * @classname UserHolder
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@UtilityClass
public class UserHolder {
     /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();



    public void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public Long getUserId() {
        return userIdTL.get();
    }



    public void clear() {
        userIdTL.remove();
    }
}
