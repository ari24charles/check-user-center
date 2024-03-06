package com.ari24charles.usercenter.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户视图
 *
 * @author ari24charles
 */
@Data
public class UserVo {

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
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatarUrl;

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

    /**
     * 创建时间
     */
    private Date createTime;
}
