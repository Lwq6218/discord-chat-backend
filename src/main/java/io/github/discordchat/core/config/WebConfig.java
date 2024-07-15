package io.github.discordchat.core.config;

import io.github.discordchat.core.constant.ApiRouterConst;
import io.github.discordchat.core.interceptor.AuthInterceptor;
import io.github.discordchat.core.interceptor.TokenParseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @classname WebConfig
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        // 权限认证拦截
        registry.addInterceptor(authInterceptor)
                // 拦截前台请求接口
                .addPathPatterns(ApiRouterConst.API_FRONT_URL_PREFIX + "/**")
                // 放行登录注册相关请求接口
                .excludePathPatterns(ApiRouterConst.API_USER_URL_PREFIX + "/sign-up",
                        ApiRouterConst.API_USER_URL_PREFIX + "/sign-in")

                .excludePathPatterns(ApiRouterConst.API_RESOURCE_URL_PREFIX + "/img_verify_code")
                .order(2);
//
//        // token解析拦截
//        registry.addInterceptor(tokenParseInterceptor)
//                // 拦截小说内容查询接口，需要解析 token 以判断该用户是否有权阅读该章节（付费章节是否已购买）
//                .addPathPatterns(ApiRouterConst + "/content/*")
//                .order(3);
//
    }
}

