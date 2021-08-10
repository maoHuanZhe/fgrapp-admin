package com.fgrapp.admin.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.admin.domain.SysRoleDo;
import com.fgrapp.admin.domain.SysRoleMenu;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.domain.SysUserRole;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SysRoleMapper
 *
 * @author fan guang rui
 * @date 2021年08月02日 13:39
 */
@Mapper
@Component
public interface SysRoleMapper extends FgrMapper<SysRoleDo> {
    List<SysRoleDo> selectRolePermissionByUserId(Long userId);
    List<SysRoleDo> selectRolesByUserName(String userName);

    IPage<List<Map<String, Object>>> getPage(Page<SysRoleDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);

    int countUserRoleByRoleId(List<Long> roleIds);

    void insetRoleMenuList(List<SysRoleMenu> list);

    void deleteRoleMenuByRoleId(Long id);

    void deleteRoleMenu(List<Long> roleIds);

    IPage<List<Map<String, Object>>> allocatedList(Page<SysUserDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);

    IPage<List<Map<String, Object>>> unallocatedList(Page<SysUserDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);

    int deleteUserRoleInfo(SysUserRole userRole);

    int cancelAuthUserAll(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);
}
