package com.ari24charles.usercenter.exception;

import com.ari24charles.usercenter.common.BaseResponse;
import com.ari24charles.usercenter.common.ResultUtils;
import com.ari24charles.usercenter.common.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @author ari24charles
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("[BusinessException!] " + e.getMessage() + ": " + e.getDescription(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeException(RuntimeException e) {
        log.error("[RuntimeException!] " + e.getMessage(), e);
        return ResultUtils.error(StatusCode.SYSTEM_ERROR, e.getMessage());
    }
}
