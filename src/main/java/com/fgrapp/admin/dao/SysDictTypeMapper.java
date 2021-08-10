package com.fgrapp.admin.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.admin.domain.SysDictTypeDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SysDictTypeMapper
 *
 * @author fan guang rui
 * @date 2021年08月10日 12:35
 */
@Mapper
@Component
public interface SysDictTypeMapper extends FgrMapper<SysDictTypeDo> {
    IPage<List<Map<String, Object>>> getPage(Page<SysDictTypeDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);
}
