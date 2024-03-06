package com.ari24charles.usercenter.common;

import lombok.Data;

import java.util.List;

/**
 * 做分页查询时，包含分页信息和数据的封装类
 *
 * @param <T> 返回数据的类型
 * @author ari24charles
 */
@Data
public class PageResponse<T> {

    /**
     * 当前页号
     */
    private Long current;

    /**
     * 页面大小
     */
    private Long size;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 查询记录
     */
    private List<T> records;
}
