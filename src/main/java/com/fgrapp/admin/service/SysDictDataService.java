package com.fgrapp.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysDictDataMapper;
import com.fgrapp.admin.domain.SysDictDataDo;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysDictDataService
 *
 * @author fan guang rui
 * @date 2021年08月05日 13:48
 */
@Service
public class SysDictDataService extends FgrService<SysDictDataMapper, SysDictDataDo> {
    public List<SysDictDataDo> dictType(Long dictType) {
        return baseMapper.selectList(new QueryWrapper<SysDictDataDo>()
                .eq("status",0).eq("dictType",dictType)
                .orderByAsc("dictSort"));
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysDictDataDo.class),map);
    }
}
