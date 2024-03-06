package com.ari24charles.usercenter.common;

/**
 * 返回工具类
 *
 * @author ari24charles
 */
public class ResultUtils {

    /**
     * 请求成功 (没有数据返回)
     *
     * @return 通用返回类
     */
    public static BaseResponse<?> success() {
        return new BaseResponse<>(StatusCode.SUCCESS);
    }

    /**
     * 请求成功 (有数据返回)
     *
     * @param data 返回数据
     * @param <T>  返回数据的类型
     * @return 通用返回类
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(StatusCode.SUCCESS, data);
    }

    /**
     * 请求失败 (用于全局异常处理中异常的处理)
     *
     * @param statusCode  状态码
     * @param description 状态码详情
     * @return 通用返回类
     */
    public static BaseResponse<?> error(StatusCode statusCode, String description) {
        return new BaseResponse<>(statusCode, description);
    }

    /**
     * 请求失败 (用于全局异常处理中自定义异常的处理)
     *
     * @param code        状态码
     * @param msg         状态码简讯
     * @param description 状态码详情
     * @return 通用返回类
     */
    public static BaseResponse<?> error(Integer code, String msg, String description) {
        return new BaseResponse<>(code, msg, description);
    }
}
