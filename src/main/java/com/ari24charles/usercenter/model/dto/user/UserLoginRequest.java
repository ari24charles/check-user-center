package com.ari24charles.usercenter.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author ari24charles
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
