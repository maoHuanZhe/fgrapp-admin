package com.fgrapp.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysOperLogMapper;
import com.fgrapp.admin.domain.SysOperLogDo;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysOperLogService
 *
 * @author fan guang rui
 * @date 2021年07月31日 12:08
 */
@Service
public class SysOperLogService extends FgrService<SysOperLogMapper, SysOperLogDo> {
    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysOperLogDo.class),map);
    }
}
