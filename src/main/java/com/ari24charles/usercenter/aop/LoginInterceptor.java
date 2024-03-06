package com.ari24charles.usercenter.aop;

import com.ari24charles.usercenter.annotation.LoginCheck;
import com.ari24charles.usercenter.utils.UserValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 用户登录校验
 *
 * @author ari24charles
 */
@Aspect
@Component
public class LoginInterceptor {

    /**
     * 拦截所有添加了 @LoginCheck 注解的方法，进行登录校验
     */
    @Around("@annotation(loginCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        // 校验用户登录状态
        UserValidator.validateLoginStatus();
        // 通过登录校验
        return joinPoint.proceed();
    }
}
