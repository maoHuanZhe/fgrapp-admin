package com.fgrapp.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysDictDataMapper;
import com.fgrapp.admin.dao.SysDictTypeMapper;
import com.fgrapp.admin.domain.SysDictDataDo;
import com.fgrapp.admin.domain.SysDictTypeDo;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysDictTypeService
 *
 * @author fan guang rui
 * @date 2021年08月10日 12:36
 */
@Service
public class SysDictTypeService extends FgrService<SysDictTypeMapper, SysDictTypeDo> {
    private final SysDictDataMapper dataMapper;

    public SysDictTypeService(SysDictDataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysDictTypeDo.class),map);
    }

    public int add(SysDictTypeDo info) {
        //校验字典类型
        if (checkDictTypeUnique(info)){
            throw new ResultException("新增字典'" + info.getDictName() + "'失败，字典类型已存在");
        }
        return baseMapper.insert(info);
    }

    private boolean checkDictTypeUnique(SysDictTypeDo info) {
        List<SysDictTypeDo> list = baseMapper.selectList(new LambdaQueryWrapper<SysDictTypeDo>()
                .eq(SysDictTypeDo::getDictType, info.getDictType()));
        return FgrUtil.checkUnique(info,list);
    }

    public SysDictTypeDo edit(SysDictTypeDo info) {
        //校验字典类型
        if (checkDictTypeUnique(info)){
            throw new ResultException("修改字典'" + info.getDictName() + "'失败，字典类型已存在");
        }
        baseMapper.updateById(info);
        return info;
    }

    public void dels(List<Long> dictTypeIds) {
        Integer count = dataMapper.selectCount(new LambdaQueryWrapper<SysDictDataDo>()
                .in(SysDictDataDo::getDictType, dictTypeIds));
        if (count > 0){
            throw new ResultException("字典类型已分配,不能删除");
        }
        baseMapper.deleteBatchIds(dictTypeIds);
    }
}
