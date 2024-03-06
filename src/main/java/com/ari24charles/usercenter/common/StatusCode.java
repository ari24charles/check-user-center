package com.ari24charles.usercenter.common;

import lombok.Getter;

/**
 * 返回状态码
 *
 * @author ari24charles
 */
@Getter
public enum StatusCode {

    SUCCESS(20000, "请求成功"),
    /**
     * 400: 客户端请求的语法错误
     */
    PARAMS_ERROR(40000, "请求参数错误"),
    /**
     * 401: 请求要求用户的身份认证
     */
    NOT_LOGIN(40100, "用户未登录"),
    NO_AUTH(40101, "用户无权限"),
    BANNED_ERROR(40102, "用户封禁"),
    NO_USER(40103, "用户不存在"),
    /**
     * 403: 服务器拒绝执行该请求
     */
    FORBIDDEN_ERROR(40300, "禁止访问"),
    /**
     * 404: 服务器无法根据请求找到资源
     */
    NOT_FOUND(40400, "资源不存在"),
    /**
     * 500: 服务器内部错误导致无法完成请求
     */
    SYSTEM_ERROR(50000, "系统异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态码简讯
     */
    private final String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
