package com.venus.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venus.common.xss.SQLFilter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 *
 * @author Tomxuetao
 */
public class Query<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (params.get(Constant.PAGE) != null) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String) params.get(Constant.ORDER_FIELD));
        String order = (String) params.get(Constant.ORDER);
        List<OrderItem> orderItemList = new ArrayList<>();
        //前端字段排序
        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
            if (Constant.ASC.equalsIgnoreCase(order)) {
                orderItemList.add(OrderItem.asc(orderField));
                page.setOrders(orderItemList);
            } else {
                orderItemList.add(OrderItem.desc(orderField));
            }
            page.setOrders(orderItemList);
            return page;
        }

        //默认排序
        if (StringUtils.isNotEmpty(defaultOrderField)) {
            if (isAsc) {
                orderItemList.add(OrderItem.asc(defaultOrderField));
                page.setOrders(orderItemList);
            } else {
                orderItemList.add(OrderItem.desc(defaultOrderField));
                page.setOrders(orderItemList);
            }
        }
        return page;
    }
}
