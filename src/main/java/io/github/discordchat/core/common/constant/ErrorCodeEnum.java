package io.github.discordchat.core.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @classname ErrorCodeEnum
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /**
     * 正确执行后的返回
     */
    OK("00000", "一切 ok"),

    /**
     * 一级宏观错误码，用户端错误
     */
    USER_ERROR("A0001", "用户端错误"),

    /**
     * 二级宏观错误码，用户注册错误
     */
    USER_REGISTER_ERROR("A0100", "用户注册错误"),

    /**
     * 用户未同意隐私协议
     */
    USER_NO_AGREE_PRIVATE_ERROR("A0101", "用户未同意隐私协议"),

    /**
     * 注册国家或地区受限
     */
    USER_REGISTER_AREA_LIMIT_ERROR("A0102", "注册国家或地区受限"),

    /**
     * 用户验证码错误
     */
    USER_VERIFY_CODE_ERROR("A0240", "用户验证码错误"),

    /**
     * 用户名已存在
     */
    USER_NAME_EXIST("A0111", "用户名已存在"),

    /**
     * 服务器名称已存在
     */
    SERVER_NAME_EXIST("A0211", "服务器名称已存在"),

    /**
     * 服务器不存在
     */
    SERVER_NOT_EXIST("A0212", "服务器不存在"),

    /**
     * 频道不存在
     */
    CHANNEL_NOT_EXIST("A0312", "频道不存在"),

    /**
     * 成员已存在
     */
    MEMBER_EXIST("A0312", "服务器不存在"),

    /**
     * 成员不存在
     */
    MEMBER_NOT_EXIST("A0312", "成员不存在"),

    /**
     * 成员不存在
     */
    MESSAGE_NOT_EXIST("A0412", "消息不存在"),

    /**
     * 成员不存在
     */
    CONVERSATION_NOT_EXIST("A0312", "会话不存在"),

    /**
     * 问题不存在
     */
    QUESTION_NOT_EXIST("A0312", "问题不存在"),

    /**
     * 回复不存在
     */
    ANSWER_NOT_EXIST("A0312", "回复不存在"),
    /**
     * 频道已存在
     */
    CHANNEL_EXIST("A0412", "频道已存在"),
    /**
     * 手机号码已注册
     */
    PHONE_NUMBER_EXIST("A0112", "手机号码已注册"),

    /**
     * 用户账号不存在
     */
    USER_ACCOUNT_NOT_EXIST("A0201", "用户账号不存在"),

    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR("A0210", "用户密码错误"),

    /**
     * 二级宏观错误码，用户请求参数错误
     */
    USER_REQUEST_PARAM_ERROR("A0400", "用户请求参数错误"),

    /**
     * 用户登录已过期
     */
    USER_LOGIN_EXPIRED("A0230", "用户登录已过期"),

    /**
     * 访问未授权
     */
    USER_UN_AUTH("A0301", "访问未授权"),

    /**
     * 用户请求服务异常
     */
    USER_REQ_EXCEPTION("A0500", "用户请求服务异常"),

    /**
     * 请求超出限制
     */
    USER_REQ_MANY("A0501", "请求超出限制"),


    /**
     * 用户上传文件异常
     */
    USER_UPLOAD_FILE_ERROR("A0700", "用户上传文件异常"),

    /**
     * 用户上传文件类型不匹配
     */
    USER_UPLOAD_FILE_TYPE_NOT_MATCH("A0701", "用户上传文件类型不匹配"),

    /**
     * 一级宏观错误码，系统执行出错
     */
    SYSTEM_ERROR("B0001", "系统执行出错"),

    /**
     * 二级宏观错误码，系统执行超时
     */
    SYSTEM_TIMEOUT_ERROR("B0100", "系统执行超时"),

    /**
     * 一级宏观错误码，调用第三方服务出错
     */
    THIRD_SERVICE_ERROR("C0001", "调用第三方服务出错"),

    /**
     * 一级宏观错误码，中间件服务出错
     */
    MIDDLEWARE_SERVICE_ERROR("C0100", "中间件服务出错");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 中文描述
     */
    private final String message;
}
