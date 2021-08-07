package com.fgrapp.admin.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.domain.SysUserRole;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SysUserMapper
 *
 * @author fan guang rui
 * @date 2021年08月01日 9:36
 */
@Mapper
@Component
public interface SysUserMapper extends FgrMapper<SysUserDo> {
    SysUserDo selectUserByUserName(String userName);

    IPage<List<Map<String, Object>>> getPage(Page<SysUserDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);

    Map<String, Object> getInfo(Long userId);

    void insertUserRoleList(List<SysUserRole> list);

    void deleteUserRoleList(Long id);

    void deleteUserRoleListByIds(List<Long> userIds);
}
