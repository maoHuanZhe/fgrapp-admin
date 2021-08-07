package com.fgrapp.base.saToken;

/**
 * StpInterfaceImpl
 *
 * @author fan guang rui
 * @date 2021年08月04日 18:00
 */

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.service.SysPermissionService;
import com.fgrapp.base.utils.FgrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义权限验证接口扩展
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private SysPermissionService service;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return service.getMenuPermission(FgrUtil.getSysUserDo());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return service.getRolePermission(FgrUtil.getSysUserDo());
    }

}
