package com.ari24charles.usercenter.utils;

import cn.hutool.core.util.StrUtil;

/**
 * SQL 工具类
 *
 * @author ari24charles
 */
public class SqlUtils {

    /**
     * 粗略防止排序字段的 SQL 注入
     *
     * @param sortField 排序字段
     * @return true -> 没有 SQL 注入, false -> 有 SQL 注入
     */
    public static boolean validSortField(String sortField) {
        if (StrUtil.isBlank(sortField)) {
            return false;
        }
        // 防止 SQL 注入
        // todo 更好的防御方法
        return !StrUtil.containsAny(sortField, "=", "(", ")", " ");
    }
}
