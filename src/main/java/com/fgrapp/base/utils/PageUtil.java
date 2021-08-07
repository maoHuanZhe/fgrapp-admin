package com.fgrapp.base.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.core.instrument.util.StringUtils;

import java.util.Map;

/**
 * PageUtil
 *
 * @author fan guang rui
 * @date 2021年06月09日 13:13
 */
public class PageUtil {
    public static  <T> Page<T> getParamPage(Map<String,Object> map, Class<T> responseType){

        int current = Integer.parseInt(map.get("pageNum").toString()) ;
        int size = Integer.parseInt(map.get("pageSize").toString());
        Page<T> page = new Page<>(current,size);

        //排序字段
        String order = (String) map.get("order");
        if (StringUtils.isNotEmpty(order)){
            String[] split = order.split(",");
            for (String s : split) {
                String[] strings = s.split("-");
                if (strings.length > 1 && "desc".equals(strings[1])){
                    page.addOrder(OrderItem.desc(strings[0]));
                }else {
                    page.addOrder(OrderItem.asc(strings[0]));
                }
            }
        }
        return page;
    }
}
