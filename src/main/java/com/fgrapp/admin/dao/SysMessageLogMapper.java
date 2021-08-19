package com.fgrapp.admin.dao;

import com.fgrapp.admin.domain.SysMessageLogDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * SysMessageLogMapper
 *
 * @author fan guang rui
 * @date 2021年08月19日 14:54
 */
@Mapper
@Component
public interface SysMessageLogMapper extends FgrMapper<SysMessageLogDo> {
}
