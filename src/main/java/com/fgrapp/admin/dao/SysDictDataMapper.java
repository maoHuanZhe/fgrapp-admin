package com.fgrapp.admin.dao;

import com.fgrapp.admin.domain.SysDictDataDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * SysDictDataMapper
 *
 * @author fan guang rui
 * @date 2021年08月05日 13:47
 */
@Mapper
@Component
public interface SysDictDataMapper extends FgrMapper<SysDictDataDo> {
}
