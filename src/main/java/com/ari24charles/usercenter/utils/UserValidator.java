package com.ari24charles.usercenter.utils;

import com.ari24charles.usercenter.common.StatusCode;
import com.ari24charles.usercenter.exception.BusinessException;
import com.ari24charles.usercenter.model.vo.UserVo;
import com.ari24charles.usercenter.constant.UserConstant;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户实体相关字段的校验操作
 *
 * @author ari24charles
 */
public class UserValidator {

    /**
     * 校验用户是否登录
     *
     * @return 登录的用户 id
     */
    public static Long validateLoginStatus() {
        // 获取请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录的用户
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        UserVo loginUser = (UserVo) userObject;
        if (loginUser == null) { // 用户未登录
            throw new BusinessException(StatusCode.NOT_LOGIN);
        }
        return loginUser.getId();
    }

    /**
     * 校验用户的账号字段
     *
     * @param username 账号
     */
    public static void validateUsername(String username) {
        // 账号的长度应该不小于4位
        if (username.length() < 4) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号过短");
        }
        // 账号的长度应该小于30位
        if (username.length() > 30) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号过长");
        }
        // 账号不能包含特殊字符 todo 替换为可读性更好的正则表达式
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
    }

    /**
     * 校验用户的密码字段
     *
     * @param password 密码
     */
    public static void validatePassword(String password) {
        // 密码应该不小于8位
        if (password.length() < 8) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码过短");
        }
        // 密码应该小于50位
        if (password.length() > 50) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码过长");
        }
    }

    // todo 昵称、电话和邮箱的校验

    /**
     * 校验用户的密码字段
     *
     * @param gender 性别
     */
    public static void validateGender(Integer gender) {
        if ((!Objects.equals(gender, UserConstant.SECRET))
                && (!Objects.equals(gender, UserConstant.BOY))
                && (!Objects.equals(gender, UserConstant.GIRL))) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "性别异常");
        }
    }
}
