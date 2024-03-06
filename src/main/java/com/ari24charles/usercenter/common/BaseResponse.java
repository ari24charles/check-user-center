package com.ari24charles.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T> 返回数据的类型
 * @author ari24charles
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码简讯
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 状态码详情
     */
    private String description;

    public BaseResponse(Integer code, String msg, T data, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(StatusCode statusCode) {
        this(statusCode.getCode(), statusCode.getMsg(), null, null);
    }

    public BaseResponse(StatusCode statusCode, T data) {
        this(statusCode.getCode(), statusCode.getMsg(), data, null);
    }

    public BaseResponse(StatusCode statusCode, String description) {
        this(statusCode.getCode(), statusCode.getMsg(), null, description);
    }

    public BaseResponse(Integer code, String msg, String description) {
        this(code, msg, null, description);
    }
}
