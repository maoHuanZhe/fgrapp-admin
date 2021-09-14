package com.fgrapp.base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.dao.ClassMapper;
import com.fgrapp.blog.domain.ClassDo;

import java.util.List;
import java.util.Map;

/**
 * FgrService
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:14
 */
public abstract class FgrService<M extends FgrMapper<T>,T> extends ServiceImpl<M,T> {
    public Map<String, Object> getStringObjectMap(Map<String, Object> map, Map<String, Object> returnMap, IPage<List<Map<String, Object>>> page, ClassMapper classMapper) {
        String getClassList = (String) map.get("getClassList");
        if (Boolean.TRUE.toString().equals(getClassList)){
            List<ClassDo> classDoList = classMapper.selectList(new QueryWrapper<>());
            returnMap.put("classList",classDoList);
        }
        returnMap.put("page",page);
        return returnMap;
    }
}
