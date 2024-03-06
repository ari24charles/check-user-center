package com.ari24charles.usercenter.controller;

import com.ari24charles.usercenter.annotation.AuthCheck;
import com.ari24charles.usercenter.annotation.LoginCheck;
import com.ari24charles.usercenter.common.BaseResponse;
import com.ari24charles.usercenter.common.IdRequest;
import com.ari24charles.usercenter.common.StatusCode;
import com.ari24charles.usercenter.exception.BusinessException;
import com.ari24charles.usercenter.model.dto.user.*;
import com.ari24charles.usercenter.model.dto.user.*;
import com.ari24charles.usercenter.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.ari24charles.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.ari24charles.usercenter.constant.UserConstant.SUPER_ADMIN_ROLE;

/**
 * 用户接口
 *
 * @author ari24charles
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 用户脱敏视图
     */
    @PostMapping("/register")
    public BaseResponse<?> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.userRegister(userRegisterRequest);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @param request          客户端请求
     * @return 用户脱敏视图
     */
    @PostMapping("/login")
    public BaseResponse<?> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null || request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.userLogin(userLoginRequest, request);
    }

    /**
     * 用户登出
     *
     * @param request 客户端请求
     * @return null
     */
    @PostMapping("/logout")
    @LoginCheck
    public BaseResponse<?> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.userLogout(request);
    }

    /**
     * 获取当前用户脱敏视图
     *
     * @param request 客户端请求
     * @return 用户脱敏视图
     */
    @GetMapping("/current")
    @LoginCheck
    public BaseResponse<?> getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.getCurrentUser(request);
    }

    /**
     * 更改用户个人信息
     *
     * @param userUpdateRequest 用户更新个人信息请求体
     * @param request           客户端请求
     * @return 用户脱敏视图
     */
    @PostMapping("/update")
    @LoginCheck
    public BaseResponse<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.updateUser(userUpdateRequest, request);
    }

    /**
     * 注销账号
     *
     * @param idRequest 包含用户 id 的请求
     * @param request   客户端请求
     * @return null
     */
    @PostMapping("/delete")
    @LoginCheck
    public BaseResponse<?> deleteUser(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.deleteUser(idRequest, request);
    }

    /**
     * 查询用户
     *
     * @param userQueryRequest 用户查询请求体
     * @return 用户脱敏视图集合
     */
    @PostMapping("/search")
    @LoginCheck
    public BaseResponse<?> searchUser(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.searchUser(userQueryRequest);
    }

    /**
     * 查询用户 [管理员]
     *
     * @param adminQueryRequest 用户查询请求体
     * @return 用户脱敏信息集合
     */
    @PostMapping("/admin/list")
    @AuthCheck(mustRole = {SUPER_ADMIN_ROLE, ADMIN_ROLE})
    public BaseResponse<?> listUser(@RequestBody AdminQueryRequest adminQueryRequest) {
        if (adminQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.listUser(adminQueryRequest);
    }

    /**
     * 改变用户角色 管理员 <-> 用户 [超级管理员]
     *
     * @param idRequest 包含用户 id 的请求
     * @return null
     */
    @PostMapping("/admin/role")
    @AuthCheck(mustRole = {SUPER_ADMIN_ROLE})
    public BaseResponse<?> switchUserRole(@RequestBody IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.switchUserRole(idRequest);
    }

    /**
     * 改变用户状态 封禁 <-> 解封 [管理员]
     *
     * @param idRequest 包含用户 id 的请求
     * @return null
     */
    @PostMapping("/admin/status")
    @AuthCheck(mustRole = {SUPER_ADMIN_ROLE, ADMIN_ROLE})
    public BaseResponse<?> switchUserStatus(@RequestBody IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        return userService.switchUserStatus(idRequest);
    }
}
