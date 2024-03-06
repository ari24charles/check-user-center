package com.ari24charles.usercenter.service;

import com.ari24charles.usercenter.common.BaseResponse;
import com.ari24charles.usercenter.common.IdRequest;
import com.ari24charles.usercenter.common.PageResponse;
import com.ari24charles.usercenter.model.dto.user.*;
import com.ari24charles.usercenter.model.entity.User;
import com.ari24charles.usercenter.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ari24charles
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-02-22 01:57:57
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 用户脱敏视图
     */
    BaseResponse<UserVo> userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求体
     * @param request          客户端请求
     * @return 用户脱敏视图
     */
    BaseResponse<UserVo> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request 客户端请求
     * @return null
     */
    BaseResponse<?> userLogout(HttpServletRequest request);

    /**
     * 获取当前用户脱敏视图
     *
     * @param request 客户端请求
     * @return 用户脱敏视图
     */
    BaseResponse<UserVo> getCurrentUser(HttpServletRequest request);

    /**
     * 更改用户个人信息
     *
     * @param userUpdateRequest 用户更新个人信息请求体
     * @param request           客户端请求
     * @return 用户脱敏视图
     */
    BaseResponse<UserVo> updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 注销账号
     *
     * @param idRequest 包含用户 id 的请求
     * @param request   客户端请求
     * @return null
     */
    BaseResponse<?> deleteUser(IdRequest idRequest, HttpServletRequest request);

    /**
     * 查询用户
     *
     * @param userQueryRequest 用户查询请求体
     * @return 用户视图集合
     */
    BaseResponse<PageResponse<UserVo>> searchUser(UserQueryRequest userQueryRequest);

    /**
     * 查询用户 [管理员]
     *
     * @param adminQueryRequest 用户查询请求体
     * @return 用户脱敏信息集合
     */
    BaseResponse<PageResponse<User>> listUser(AdminQueryRequest adminQueryRequest);

    /**
     * 改变用户角色 管理员 <-> 用户 [超级管理员]
     *
     * @param idRequest 包含用户 id 的请求
     * @return null
     */
    BaseResponse<?> switchUserRole(IdRequest idRequest);

    /**
     * 改变用户状态 封禁 <-> 解封 [管理员]
     *
     * @param idRequest 包含用户 id 的请求
     * @return null
     */
    BaseResponse<?> switchUserStatus(IdRequest idRequest);

    /**
     * 将用户信息处理成用户视图
     *
     * @param user 用户信息
     * @return 用户视图
     */
    UserVo getUserVoByUser(User user);

    /**
     * 从数据库中获取最新的用户数据
     *
     * @param request 客户端请求
     * @return 用户信息
     */
    User getLatestUser(HttpServletRequest request);

    /**
     * 用户视图脱敏
     *
     * @param userVo 用户视图
     * @return 脱敏后的用户视图
     */
    UserVo desensitize(UserVo userVo);

    /**
     * 用户信息批量脱敏
     *
     * @param userList 用户集合
     * @return 脱敏后的用户集合
     */
    List<User> desensitizeBatch(List<User> userList);
}
