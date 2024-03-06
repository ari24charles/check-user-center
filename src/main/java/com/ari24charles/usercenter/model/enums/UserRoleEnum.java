package com.ari24charles.usercenter.model.enums;

import cn.hutool.core.util.StrUtil;
import com.ari24charles.usercenter.common.StatusCode;
import com.ari24charles.usercenter.exception.BusinessException;
import lombok.Getter;

/**
 * 用户角色枚举类
 *
 * @author ari24charles
 */
@Getter
public enum UserRoleEnum {

    SUPER_ADMIN(0, "super_admin"),
    ADMIN(1, "admin"),
    USER(2, "user");

    /**
     * 角色代码
     */
    private final Integer roleCode;

    /**
     * 角色名
     */
    private final String roleText;

    UserRoleEnum(Integer roleCode, String roleText) {
        this.roleCode = roleCode;
        this.roleText = roleText;
    }

    /**
     * 根据角色名获取角色代码
     *
     * @param roleText 角色名
     * @return 角色代码
     */
    public static Integer getRoleCodeByText(String roleText) {
        if (StrUtil.isBlank(roleText)) {
            // 传入角色名为空，检查代码
            throw new BusinessException(StatusCode.SYSTEM_ERROR);
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.roleText.equals(roleText)) {
                return anEnum.getRoleCode();
            }
        }
        // 传入角色名未定义，检查代码
        throw new BusinessException(StatusCode.SYSTEM_ERROR);
    }
}
