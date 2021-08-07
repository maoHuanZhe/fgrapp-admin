package com.fgrapp.admin.service;

import com.fgrapp.admin.domain.SysUserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author fan guang rui
 * @date 2021年06月10日 14:26
 */
@Component
public class SysPermissionService
{
    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    public List<String> getRolePermission(SysUserDo user)
    {
        List<String> roles = new ArrayList<>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public List<String> getMenuPermission(SysUserDo user){
        List<String> perms = new ArrayList<>();
        // 管理员拥有所有权限
        if (user.isAdmin()){
            perms.add("*:*:*");
        }else{
            perms.addAll(menuService.selectMenuPermsByUserId(user.getId()));
        }
        return perms;
    }
}
