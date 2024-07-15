package io.github.discordchat.core.common.exception;

import io.github.discordchat.core.common.constant.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @classname BusinessException
 * @description TODO
 * @date 2024/6/13
 * @created by lwq
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends  RuntimeException {
      private final ErrorCodeEnum errorCodeEnum;

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        // 不调用父类 Throwable的fillInStackTrace() 方法生成栈追踪信息，提高应用性能
        // 构造器之间的调用必须在第一行
        super(errorCodeEnum.getMessage(), null, false, false);
        this.errorCodeEnum = errorCodeEnum;
    }
}
