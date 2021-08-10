package com.fgrapp.admin.dao;

import com.fgrapp.admin.domain.SysMenuDo;
import com.fgrapp.base.dao.FgrMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SysMenuMapper
 *
 * @author fan guang rui
 * @date 2021年07月31日 15:29
 */
@Mapper
@Component
public interface SysMenuMapper extends FgrMapper<SysMenuDo> {
    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    List<String> selectMenuPermsByUserId(Long userId);

    List<SysMenuDo> selectMenuTreeAll();

    List<SysMenuDo> selectMenuTreeByUserId(Long userId);

    List<SysMenuDo> selectMenuList(SysMenuDo info);

    List<SysMenuDo> selectMenuListByUserId(SysMenuDo info, @Param("userId") Long userId);

    int checkMenuExistRole(Long menuId);

    List<Integer> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);
}
