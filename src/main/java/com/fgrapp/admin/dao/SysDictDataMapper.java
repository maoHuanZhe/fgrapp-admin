package com.fgrapp.admin.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.admin.domain.SysDictDataDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SysDictDataMapper
 *
 * @author fan guang rui
 * @date 2021年08月05日 13:47
 */
@Mapper
@Component
public interface SysDictDataMapper extends FgrMapper<SysDictDataDo> {
    IPage<List<Map<String, Object>>> getPage(Page<SysDictDataDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);
}
