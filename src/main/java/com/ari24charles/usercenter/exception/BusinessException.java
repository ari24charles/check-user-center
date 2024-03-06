package com.ari24charles.usercenter.exception;

import com.ari24charles.usercenter.common.StatusCode;
import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author ari24charles
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态码详情
     */
    private final String description;

    public BusinessException(Integer code, String msg, String description) {
        super(msg);
        this.code = code;
        this.description = description;
    }

    public BusinessException(StatusCode statusCode) {
        this(statusCode.getCode(), statusCode.getMsg(), null);
    }

    public BusinessException(StatusCode statusCode, String description) {
        this(statusCode.getCode(), statusCode.getMsg(), description);
    }
}
