package com.ari24charles.usercenter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户身份校验的注解
 *
 * @author ari24charles
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 执行方法需要的权限列表
     */
    String[] mustRole() default {};
}
