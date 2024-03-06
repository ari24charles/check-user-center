package com.ari24charles.usercenter.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ari24charles.usercenter.common.*;
import com.ari24charles.usercenter.exception.BusinessException;
import com.ari24charles.usercenter.model.dto.user.*;
import com.ari24charles.usercenter.model.enums.UserRoleEnum;
import com.ari24charles.usercenter.model.vo.UserVo;
import com.ari24charles.usercenter.utils.SqlUtils;
import com.ari24charles.usercenter.utils.UserValidator;
import com.ari24charles.usercenter.constant.CommonConstant;
import com.ari24charles.usercenter.constant.UserConstant;
import com.ari24charles.usercenter.mapper.UserMapper;
import com.ari24charles.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ari24charles.usercenter.model.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ari24charles
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-02-22 01:57:57
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public BaseResponse<UserVo> userRegister(UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String check = userRegisterRequest.getCheck();
        String phone = userRegisterRequest.getPhone();
        // 1. 校验
        if (StrUtil.hasBlank(username, password, check, phone)) { // 非空校验
            throw new BusinessException(StatusCode.PARAMS_ERROR, "参数为空");
        }
        UserValidator.validateUsername(username); // 账号校验
        UserValidator.validatePassword(password); // 密码校验
        if (!password.equals(check)) { // 密码应该和校验密码相同
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码应该和校验密码相同");
        }
        long count = this.count(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (count > 0) { // 手机号不能重复
            throw new BusinessException(StatusCode.PARAMS_ERROR, "该手机号已经被注册了");
        }
        count = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count > 0) { // 账号不能重复
            throw new BusinessException(StatusCode.PARAMS_ERROR, "该账号已存在");
        }
        // 2. 字段处理
        String encryptPassword = DigestUtil.md5Hex(CommonConstant.SALT + password); // 密码加密
        String randomNickname = IdUtil.simpleUUID().substring(0, 16); // 随机生成用户昵称
        // 3. 插入数据
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        user.setPhone(phone);
        user.setNickname(randomNickname);
        boolean isSuccess = this.save(user);
        if (!isSuccess) {
            throw new BusinessException(StatusCode.OPERATION_ERROR);
        }
        // 4. 获取新用户视图
        User newUser = this.getById(user.getId());
        UserVo userVo = getUserVoByUser(newUser);
        // 5. 脱敏
        UserVo desensitizedUserVo = desensitize(userVo);
        return ResultUtils.success(desensitizedUserVo);
    }

    @Override
    public BaseResponse<UserVo> userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null || request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        // 1. 校验
        if (StrUtil.hasBlank(username, password)) { // 非空校验
            throw new BusinessException(StatusCode.PARAMS_ERROR, "参数为空");
        }
        UserValidator.validateUsername(username); // 账号校验
        UserValidator.validatePassword(password); // 密码校验
        // 2. 获取加密后的密码
        String encryptPassword = DigestUtil.md5Hex(CommonConstant.SALT + password);
        // 3. 查询数据库
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getPassword, encryptPassword));
        if (user == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 4. 校验用户是否封禁
        if (user.getStatus().equals(UserConstant.BANNED_STATUS)) {
            throw new BusinessException(StatusCode.BANNED_ERROR);
        }
        // 5. 获取用户视图
        UserVo userVo = getUserVoByUser(user);
        // 6. 脱敏
        UserVo desensitizedUserVo = desensitize(userVo);
        // 7. 记录用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, desensitizedUserVo);
        return ResultUtils.success(desensitizedUserVo);
    }

    @Override
    public BaseResponse<?> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.success();
    }

    @Override
    public BaseResponse<UserVo> getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        // 获取当前用户
        User user = getLatestUser(request);
        // 获取用户脱敏视图
        UserVo userVo = getUserVoByUser(user);
        UserVo desensitizedUserVo = desensitize(userVo);
        // 更新用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, desensitizedUserVo);
        return ResultUtils.success(desensitizedUserVo);
    }

    @Override
    public BaseResponse<UserVo> updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        // 1. 获取当前用户
        User user = getLatestUser(request);
        // 2. 获取请求中的参数
        String username = userUpdateRequest.getUsername();
        String password = userUpdateRequest.getPassword();
        String nickname = userUpdateRequest.getNickname();
        String phone = userUpdateRequest.getPhone();
        String email = userUpdateRequest.getEmail();
        String avatarUrl = userUpdateRequest.getAvatarUrl();
        Integer gender = userUpdateRequest.getGender();
        // 3. 校验参数
        if (StrUtil.isAllBlank(username, password, nickname, phone, email, avatarUrl)) {
            if (gender == null) { // 所有参数为空
                throw new BusinessException(StatusCode.PARAMS_ERROR, "所有参数都为空");
            }
        }
        if (gender != null) {
            UserValidator.validateGender(gender);
        }
        if (password != null) {
            UserValidator.validatePassword(password);
        }
        // 账号不能重复
        if (username != null && !username.equals(user.getUsername())) {
            UserValidator.validateUsername(username);
            long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
            if (count > 0) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "该账号已存在");
            }
        }
        if (phone != null && !phone.equals(user.getPhone())) {
            // 手机号不能重复
            long count = this.count(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
            if (count > 0) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "该手机号已经被注册了");
            }
        }
        // 4. 获取加密后的密码
        String encryptPassword = DigestUtil.md5Hex(CommonConstant.SALT + password);
        // 5. 动态更新
        boolean isSuccess = this.update(new LambdaUpdateWrapper<User>()
                .set(username != null, User::getUsername, username)
                .set(password != null, User::getPassword, encryptPassword)
                .set(nickname != null, User::getNickname, nickname)
                .set(phone != null, User::getPhone, phone)
                .set(email != null, User::getEmail, email)
                .set(avatarUrl != null, User::getAvatarUrl, avatarUrl)
                .set(gender != null, User::getGender, gender)
                .eq(User::getId, user.getId()));
        if (!isSuccess) {
            throw new BusinessException(StatusCode.OPERATION_ERROR);
        }
        // 6. 得到该用户的最新个人信息并脱敏
        User updatedUser = this.getById(user.getId());
        UserVo userVo = getUserVoByUser(updatedUser);
        UserVo desensitizedUserVo = desensitize(userVo);
        // 7. 将脱敏视图存于登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, desensitizedUserVo);
        return ResultUtils.success(desensitizedUserVo);
    }

    @Override
    public BaseResponse<?> deleteUser(IdRequest idRequest, HttpServletRequest request) {
        if (idRequest == null || request == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        Long userId = idRequest.getId();
        if (userId == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "参数为空");
        }
        // 1. 获取当前用户
        User currentUser = getLatestUser(request);
        // 2. 如果当前用户 id 与传入的 userId 不同，则没有注销他人账号的权限
        if (!currentUser.getId().equals(userId)) {
            throw new BusinessException(StatusCode.NO_AUTH, "注销失败");
        }
        boolean isSuccess = this.removeById(userId);
        if (!isSuccess) {
            throw new BusinessException(StatusCode.OPERATION_ERROR, "用户已注销");
        }
        // 清除 Session
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.success();
    }

    @Override
    public BaseResponse<PageResponse<UserVo>> searchUser(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        // 1. 获取参数
        String username = userQueryRequest.getUsername();
        String nickname = userQueryRequest.getNickname();
        Integer currentPage = userQueryRequest.getCurrent();
        Integer pageSize = userQueryRequest.getPageSize();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        // 2. 动态查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username)
                .or().like(StrUtil.isNotBlank(nickname), "nickname", nickname)
                .orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        // 3. 分页查询
        IPage<User> page = new Page<>(currentPage, pageSize);
        IPage<User> userPage = this.page(page, queryWrapper);
        // 4. 组装返回结果
        PageResponse<UserVo> pageResponse = new PageResponse<>();
        pageResponse.setCurrent(userPage.getCurrent());
        pageResponse.setSize(userPage.getSize());
        pageResponse.setTotal(userPage.getTotal());
        // 转换为用户视图并脱敏
        List<User> records = userPage.getRecords();
        List<UserVo> userVoList = new ArrayList<>();
        for (User user : records) {
            UserVo userVo = getUserVoByUser(user);
            UserVo desensitizedUserVo = desensitize(userVo);
            userVoList.add(desensitizedUserVo);
        }
        pageResponse.setRecords(userVoList);
        return ResultUtils.success(pageResponse);
    }

    @Override
    public BaseResponse<PageResponse<User>> listUser(AdminQueryRequest adminQueryRequest) {
        if (adminQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        // 1. 获取参数
        Long id = adminQueryRequest.getId();
        String username = adminQueryRequest.getUsername();
        String nickname = adminQueryRequest.getNickname();
        Integer gender = adminQueryRequest.getGender();
        Integer status = adminQueryRequest.getStatus();
        Integer role = adminQueryRequest.getRole();
        Integer currentPage = adminQueryRequest.getCurrent();
        Integer pageSize = adminQueryRequest.getPageSize();
        String sortField = adminQueryRequest.getSortField();
        String sortOrder = adminQueryRequest.getSortOrder();
        // 2. 动态查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id)
                .like(StrUtil.isNotBlank(username), "username", username)
                .like(StrUtil.isNotBlank(nickname), "nickname", nickname)
                .eq(gender != null, "gender", gender)
                .eq(status != null, "status", status)
                .eq(role != null, "role", role)
                .orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        // 3. 分页查询
        IPage<User> page = new Page<>(currentPage, pageSize);
        IPage<User> userPage = this.page(page, queryWrapper);
        // 4. 组装返回结果
        PageResponse<User> pageResponse = new PageResponse<>();
        pageResponse.setCurrent(userPage.getCurrent());
        pageResponse.setSize(userPage.getSize());
        pageResponse.setTotal(userPage.getTotal());
        // 脱敏
        List<User> records = userPage.getRecords();
        pageResponse.setRecords(desensitizeBatch(records));
        return ResultUtils.success(pageResponse);
    }

    @Override
    public BaseResponse<?> switchUserRole(IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        User updateUser = new User();
        updateUser.setId(idRequest.getId());
        User user = this.getById(idRequest.getId());
        if (user == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在");
        }
        if (user.getRole().equals(UserRoleEnum.getRoleCodeByText(UserConstant.ADMIN_ROLE))) {
            updateUser.setRole(UserRoleEnum.getRoleCodeByText(UserConstant.DEFAULT_ROLE));
        } else {
            if (user.getRole().equals(UserRoleEnum.getRoleCodeByText(UserConstant.DEFAULT_ROLE))) {
                updateUser.setRole(UserRoleEnum.getRoleCodeByText(UserConstant.ADMIN_ROLE));
            } else {
                throw new BusinessException(StatusCode.OPERATION_ERROR, "用户角色异常");
            }
        }
        boolean isSuccess = this.updateById(updateUser);
        if (!isSuccess) {
            throw new BusinessException(StatusCode.OPERATION_ERROR);
        }
        return ResultUtils.success();
    }

    @Override
    public BaseResponse<?> switchUserStatus(IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "请求为空");
        }
        User updateUser = new User();
        updateUser.setId(idRequest.getId());
        User user = this.getById(idRequest.getId());
        if (user == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在");
        }
        if (user.getStatus().equals(UserConstant.DEFAULT_STATUS)) {
            updateUser.setStatus(UserConstant.BANNED_STATUS);
        } else {
            if (user.getStatus().equals(UserConstant.BANNED_STATUS)) {
                updateUser.setStatus(UserConstant.DEFAULT_STATUS);
            } else {
                throw new BusinessException(StatusCode.OPERATION_ERROR, "用户状态异常");
            }
        }
        boolean isSuccess = this.updateById(updateUser);
        if (!isSuccess) {
            throw new BusinessException(StatusCode.OPERATION_ERROR);
        }
        return ResultUtils.success();
    }

    @Override
    public UserVo getUserVoByUser(User user) {
        if (user == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在");
        }
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setUsername(user.getUsername());
        userVo.setNickname(user.getNickname());
        userVo.setPhone(user.getPhone());
        userVo.setEmail(user.getEmail());
        userVo.setAvatarUrl(user.getAvatarUrl());
        userVo.setGender(user.getGender());
        userVo.setStatus(user.getStatus());
        userVo.setRole(user.getRole());
        userVo.setCreateTime(user.getCreateTime());
        return userVo;
    }

    @Override
    public User getLatestUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        UserVo currentUser = (UserVo) userObject;
        // 存在 Session 中的用户信息不一定是最新的
        User user = this.getById(currentUser.getId());
        if (user == null) {
            request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE); // 直接登出
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户已注销");
        }
        if (user.getStatus().equals(UserConstant.BANNED_STATUS)) {
            request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE); // 直接登出
            throw new BusinessException(StatusCode.BANNED_ERROR);
        }
        return user;
    }

    @Override
    public UserVo desensitize(UserVo userVo) {
        if (userVo == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在");
        }
        userVo.setPhone(DesensitizedUtil.mobilePhone(userVo.getPhone()));
        String email = userVo.getEmail();
        if (!StrUtil.isBlank(email)) {
            userVo.setEmail(StrUtil.hide(email, email.length() / 2, email.length()));
        }
        return userVo;
    }

    @Override
    public List<User> desensitizeBatch(List<User> userList) {
        if (userList == null) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在");
        }
        for (User user : userList) {
            user.setPassword(null);
            user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
            String email = user.getEmail();
            if (!StrUtil.isBlank(email)) {
                user.setEmail(StrUtil.hide(email, email.length() / 2, email.length()));
            }
            user.setIsDeleted(null);
        }
        return userList;
    }
}
