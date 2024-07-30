package io.github.discordchat.core.constant;

import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import io.github.discordchat.core.common.exception.BusinessException;
import jdk.dynalink.beans.StaticClass;
import lombok.Getter;

/**
 * @classname DatabaseConst
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
public class DatabaseConst {

    /**
     * 用户信息表
     */
    public static class UserProfileTable {

        private UserProfileTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_USERNAME = "name";

    }

    /**
     * 服务器信息表
     */
    public static class ServerInfoTable {

        private ServerInfoTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_SERVERNAME = "name";
        public static final String COLUMN_PROFILE_ID = "profile_id";
        public static final String COLUMN_INVITE_CODE = "invite_code";

    }

    /**
     * 成员信息表
     */
    public static class MemberInfoTable {

        private MemberInfoTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_PROFILE_ID = "profile_id";
        public static final String COLUMN_SERVER_ID = "server_id";

    }

    /**
     * 频道信息表
     */
    public static class ChannelInfoTable {

        private ChannelInfoTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_PROFILE_ID = "profile_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_CHANNEL_NAME = "name";
        public static final String DEFAULT_CHANNEL_NAME = "general";

    }

    /**
     * 交流信息表
     */
    public static class ConversationTable {

        private ConversationTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_CONVERSATION_ONE_ID = "member_one";
        public static final String COLUMN_CONVERSATION_TWO_ID = "member_two";

    }

    /**
     * 消息表
     */
    public static class MessageTable {

        private MessageTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_CHANNEL_ID = "channel_id";
        public static final String COLUMN_MEMBER_ID = "member_id";
    }

    /**
     * 直接消息表
     */
    public static class DirectMessageTable {

        private DirectMessageTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_CONVERSATION_ID = "conversation_id";
        public static final String COLUMN_MEMBER_ID = "member_id";
    }

    /**
     * 答复表
     */
    public static class AnswerTable {

        private AnswerTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_QUESTION_ID = "question_id";
        public static final String COLUMN_PROFILE_ID = "profile_id";
    }


    /**
     * 问题表
     */
    public static class QuestionTable {

        private QuestionTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_PROFILE_ID = "profile_id";
        public static final String COLUMN_UPVOTES = "upvotes";
        public static final String COLUMN_DOWNVOTES = "downvotes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ANSWER_COUNT = "answer_count";
    }

    /**
     * 问题-标签表
     */
    public static class QuestionTagTable {

        private QuestionTagTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_QUESTION_ID = "question_id";
        public static final String COLUMN_TAG_ID = "tag_id";
    }

    /**
     * 标签表
     */
    public static class TagTable {
        private TagTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_TAG_NAME = "name";
        public static final String COLUMN_QUESTION_COUNT = "question_count";
    }

    /**
     * 标签表
     */
    public static class ActionTable {

        private ActionTable() {
            throw new IllegalStateException(SystemConfigConst.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_QUESTION_ID = "question_id";
        public static final String COLUMN_ANSWER_ID = "answer_id";
        public static final String COLUMN_PROFILE_ID = "profile_id";
        public static final String COLUMN_ACTION_TYPE = "action_type";
    }

    /**
     * 动作类型
     */
    @Getter
    public enum ActionTypeEnum {
        FAVORITE(0),
        UPVOTE(1),
        DOWNVOTE(2);

        private final int value;

        ActionTypeEnum(int value) {
            this.value = value;
        }

        public static ActionTypeEnum fromValue(Integer value) {
            for (ActionTypeEnum type : ActionTypeEnum.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 成员角色
     */
    @Getter
    public enum MemberRoleEnum {
        ADMIN(0),
        MODERATOR(1),
        GUEST(2);

        private final int value;

        MemberRoleEnum(int value) {
            this.value = value;
        }

        public static MemberRoleEnum fromValue(Integer value) {
            for (MemberRoleEnum role : MemberRoleEnum.values()) {
                if (role.getValue() == value) {
                    return role;
                }
            }
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 成员角色
     */
    @Getter
    public enum ChannelTypeEnum {
        TEXT(0),
        AUDIO(1),
        VIDEO(2);

        private final int value;

        ChannelTypeEnum(int type) {
            this.value = type;
        }

        public static ChannelTypeEnum fromValue(Integer value) {
            for (ChannelTypeEnum type : ChannelTypeEnum.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new BusinessException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
        }
    }

    /**
     * 通用列枚举类
     */
    @Getter
    public enum CommonColumnEnum {

        ID("id"),
        SORT("sort"),
        CREATE_TIME("create_time"),
        UPDATE_TIME("update_time");

        private final String name;

        CommonColumnEnum(String name) {
            this.name = name;
        }

    }


    /**
     * SQL语句枚举类
     */
    @Getter
    public enum SqlEnum {

        LIMIT_1("limit 1"),
        LIMIT_2("limit 2"),
        LIMIT_5("limit 5"),
        LIMIT_30("limit 30"),
        LIMIT_500("limit 500");

        private final String sql;

        SqlEnum(String sql) {
            this.sql = sql;
        }

    }
}
