package com.ari24charles.usercenter.common;

import com.ari24charles.usercenter.constant.CommonConstant;
import lombok.Data;

/**
 * 分页信息请求体 (用于被继承，作为分页查询请求体的必要部分)
 *
 * @author ari24charles
 */
@Data
public class PageRequest {

    /**
     * 当前页号 (默认第一页)
     */
    private Integer current = 1;

    /**
     * 页面大小 (默认每页十条)
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式 (ASC || DESC)
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
