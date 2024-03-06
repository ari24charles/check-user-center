package com.ari24charles.usercenter.model.dto.user;

import com.ari24charles.usercenter.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户用于分页查询请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * 账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;
}
