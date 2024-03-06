package com.ari24charles.usercenter.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author ari24charles
 */
@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 校验密码
     */
    private String check;

    /**
     * 电话
     */
    private String phone;
}
