package com.ari24charles.usercenter.constant;

/**
 * 用户常量
 *
 * @author ari24charles
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户角色: super_admin -> 超级管理员, admin -> 管理员, user -> 普通用户
     */
    String SUPER_ADMIN_ROLE = "super_admin";
    String ADMIN_ROLE = "admin";
    String DEFAULT_ROLE = "user";

    /**
     * 用户状态: 0 -> 正常, 1 -> 封禁
     */
    Integer DEFAULT_STATUS = 0;
    Integer BANNED_STATUS = 1;

    /**
     * 性别: 0 -> 保密, 1 -> 男, 2 -> 女
     */
    Integer SECRET = 0;
    Integer BOY = 1;
    Integer GIRL = 2;
}
