package io.github.discordchat.manager.redis;

import io.github.discordchat.core.common.util.ImageVerifyCodeUtil;
import io.github.discordchat.core.constant.CacheConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * @classname VerifyCodeManager
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeManager {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 生成图形验证码，并放入 Redis 中
     */
    public String genImgVerifyCode(String sessionId) throws IOException {
        String verifyCode = ImageVerifyCodeUtil.getRandomVerifyCode(4);
        String img = ImageVerifyCodeUtil.genVerifyCodeImg(verifyCode);
        stringRedisTemplate.opsForValue().set(CacheConst.IMG_VERIFY_CODE_CACHE_KEY + sessionId,
                verifyCode, Duration.ofMinutes(5));
        return img;
    }

    /**
     * 校验图形验证码
     */
    public boolean imgVerifyCodeOk(String sessionId, String verifyCode) {
        return Objects.equals(stringRedisTemplate.opsForValue()
                .get(CacheConst.IMG_VERIFY_CODE_CACHE_KEY + sessionId), verifyCode);
    }

    /**
     * 从 Redis 中删除验证码
     */
    public void removeImgVerifyCode(String sessionId) {
        stringRedisTemplate.delete(CacheConst.IMG_VERIFY_CODE_CACHE_KEY + sessionId);
    }

}
