package io.github.discordchat.core.common.constant;

import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * @classname CommonConst
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@UtilityClass
public class CommonConst {
     /**
     * 是
     */
    public static final Integer YES = 1;
    public static final String TRUE = "true";


    /**
     * 否
     */
    public static final Integer NO = 0;
    public static final String FALSE = "false";

    /**
     * 性别常量
     */
    public enum SexEnum {

        /**
         * 男
         */
        MALE(0, "男"),

        /**
         * 女
         */
        FEMALE(1, "女");

        SexEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private final int code;
        private final String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }
    /**
     * Filter枚举
     */
    @Getter
    public enum FilterEnum {
        NEWEST("newest", "最新"),
        RECOMMEND("recommended", "推荐"),
        FREQUENT("frequent", "热门"),
        UNANSWERED("unanswered", "未回答");

        private final String value;

        private final String desc;

        FilterEnum(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }


    }
}
