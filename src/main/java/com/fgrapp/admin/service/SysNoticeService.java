package com.fgrapp.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysNoticeMapper;
import com.fgrapp.admin.domain.SysNoticeDo;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysNoticeService
 *
 * @author fan guang rui
 * @date 2021年08月11日 12:38
 */
@Service
public class SysNoticeService extends FgrService<SysNoticeMapper, SysNoticeDo> {
    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysNoticeDo.class),map);
    }
}
