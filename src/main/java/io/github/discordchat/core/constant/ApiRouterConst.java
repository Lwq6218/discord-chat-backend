package io.github.discordchat.core.constant;

/**
 * @classname ApiRouterConst
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
public class ApiRouterConst {
    private ApiRouterConst() {
        throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
    }

    /**
     * API请求路径前缀
     */
    public static final String API_URL_PREFIX = "/api";

    /**
     * 前台门户系统请求路径前缀
     */
    public static final String API_FRONT_URL_PREFIX = API_URL_PREFIX + "/front";

    /**
     * 用户模块请求路径前缀
     */
    public static final String API_USER_URL_PREFIX = API_FRONT_URL_PREFIX + "/user";

    /**
     * 服务器模块请求路径前缀
     */
    public static final String API_SERVER_URL_PREFIX = API_FRONT_URL_PREFIX + "/server";

    /**
     * 成员器模块请求路径前缀
     */
    public static final String API_MEMBER_URL_PREFIX = API_FRONT_URL_PREFIX + "/member";

    /**
     * 频道模块请求路径前缀
     */
    public static final String API_CHANNEL_URL_PREFIX = API_FRONT_URL_PREFIX + "/channel";

    /**
     * 会话模块请求路径前缀
     */
    public static final String API_CONVERSATION_URL_PREFIX = API_FRONT_URL_PREFIX + "/conversation";

    /**
     * 会话模块请求路径前缀
     */
    public static final String API_LIVE_KIT_URL_PREFIX = API_FRONT_URL_PREFIX + "/livekit";

    /**
     * 消息模块请求路径前缀
     */
    public static final String API_MESSAGE_URL_PREFIX = API_FRONT_URL_PREFIX + "/message";


    /**
     * 直接消息模块请求路径前缀
     */
    public static final String API_DIRECT_MESSAGE_URL_PREFIX = API_FRONT_URL_PREFIX + "/direct-message";

    /**
     * 问题模块请求路径前缀
     */
    public static final String API_QUESTION_URL_PREFIX = API_FRONT_URL_PREFIX + "/question";

    /**
     * 回复模块请求路径前缀
     */
    public static final String API_ANSWER_URL_PREFIX = API_FRONT_URL_PREFIX + "/answer";
     /**
     * 标签模块请求路径前缀
     */
    public static final String API_TAG_URL_PREFIX = API_FRONT_URL_PREFIX + "/tag";

    /**
     * 动作模块请求路径前缀
     */
    public static final String API_ACTION_URL_PREFIX = API_FRONT_URL_PREFIX + "/action";

    /**
     * AI模块请求路径前缀
     */
    public static final String API_OPENAI_URL_PREFIX = API_FRONT_URL_PREFIX + "/openai";
    /**
     * 资源（图片/视频/文档）模块请求路径前缀
     */
    public static final String API_RESOURCE_URL_PREFIX = API_FRONT_URL_PREFIX + "/resource";


}
