package com.fgrapp.admin.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.admin.domain.SysOperLogDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SysOperLogMapper
 *
 * @author fan guang rui
 * @date 2021年07月31日 12:04
 */
@Mapper
@Component
public interface SysOperLogMapper extends FgrMapper<SysOperLogDo> {
    IPage<List<Map<String, Object>>> getPage(Page<SysOperLogDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);
}
