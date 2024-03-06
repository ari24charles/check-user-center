package com.ari24charles.usercenter.model.dto.user;

import com.ari24charles.usercenter.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 管理员用于分页查询请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminQueryRequest extends PageRequest implements Serializable {

    /**
     * 雪花算法主键
     */
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别: 0 -> 保密, 1 -> 男, 2 -> 女
     */
    private Integer gender;

    /**
     * 用户状态: 0 -> 正常, 1 -> 封禁
     */
    private Integer status;

    /**
     * 用户角色: 0 -> 超级管理员, 1 -> 管理员, 2 -> 普通用户
     */
    private Integer role;
}
