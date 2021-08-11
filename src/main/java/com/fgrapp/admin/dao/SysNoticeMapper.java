package com.fgrapp.admin.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.admin.domain.SysNoticeDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SysNoticeMapper
 *
 * @author fan guang rui
 * @date 2021年08月11日 12:37
 */
@Mapper
@Component
public interface SysNoticeMapper extends FgrMapper<SysNoticeDo> {
    IPage<List<Map<String, Object>>> getPage(Page<SysNoticeDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);
}
