package com.ari24charles.usercenter.aop;

import com.ari24charles.usercenter.annotation.AuthCheck;
import com.ari24charles.usercenter.common.StatusCode;
import com.ari24charles.usercenter.exception.BusinessException;
import com.ari24charles.usercenter.model.entity.User;
import com.ari24charles.usercenter.model.enums.UserRoleEnum;
import com.ari24charles.usercenter.service.UserService;
import com.ari24charles.usercenter.utils.UserValidator;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户权限校验
 *
 * @author ari24charles
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 拦截所有添加了 @AuthCheck 注解的方法，进行鉴权操作
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 校验用户登录状态
        Long userId = UserValidator.validateLoginStatus();
        User loginUser = userService.getById(userId);
        if (loginUser == null) { // 用户不存在
            throw new BusinessException(StatusCode.NO_USER);
        }
        // 获取方法所需的权限列表
        String[] mustRoles = authCheck.mustRole();
        if (mustRoles == null || mustRoles.length == 0) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR);
        }
        // 鉴权，多个具有权限的角色中有一个角色即可放行
        for (String role : mustRoles) {
            if (StringUtils.isNotBlank(role)) { // 要求的角色非空
                if (loginUser.getRole().equals(UserRoleEnum.getRoleCodeByText(role))) {
                    return joinPoint.proceed(); // 登录用户的角色与要求的角色相同
                }
            }
        }
        // 所有角色都不满足，则通不过权限校验
        throw new BusinessException(StatusCode.NO_AUTH);
    }
}
