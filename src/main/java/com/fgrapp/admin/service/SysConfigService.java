package com.fgrapp.admin.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysConfigMapper;
import com.fgrapp.admin.domain.SysConfigDo;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.exception.ResultException;
import com.fgrapp.base.result.Result;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysConfigService
 *
 * @author fan guang rui
 * @date 2021年08月06日 21:27
 */
@Service
public class SysConfigService extends FgrService<SysConfigMapper, SysConfigDo> {
    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return  baseMapper.getPage(PageUtil.getParamPage(map,SysConfigDo.class),map);
    }

    public String getConfigKey(String configKey) {
        SysConfigDo sysConfigDo = baseMapper.selectOne(new LambdaQueryWrapper<SysConfigDo>()
                .eq(SysConfigDo::getConfigKey, configKey));
        if (ObjectUtil.isNotEmpty(sysConfigDo)){
            return sysConfigDo.getConfigValue();
        }
        return StrUtil.EMPTY;
    }

    public int add(SysConfigDo info) {
        if (checkConfigKeyUnique(info)){
            throw new ResultException("新增参数'" + info.getConfigName() + "'失败，参数键名已存在");
        }
        return baseMapper.insert(info);
    }

    private boolean checkConfigKeyUnique(SysConfigDo info){
        List<SysConfigDo> sysConfigDos = baseMapper.selectList(new LambdaQueryWrapper<SysConfigDo>()
                .eq(SysConfigDo::getConfigName, info.getConfigName()));
        return FgrUtil.checkUnique(info, sysConfigDos);
    }

    public SysConfigDo edit(SysConfigDo info) {
        if (checkConfigKeyUnique(info)){
            throw new ResultException("修改参数'" + info.getConfigName() + "'失败，参数键名已存在");
        }
        baseMapper.updateById(info);
        return info;
    }

    public int deleteConfigByIds(List<Long> configIds) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<SysConfigDo>()
                .in(SysConfigDo::getId, configIds)
                .eq(SysConfigDo::getConfigType, UserConstants.YES));
        if (count > 0){
            throw new ResultException("存在内置参数不能删除");
        }
        return baseMapper.deleteBatchIds(configIds);
    }
}
