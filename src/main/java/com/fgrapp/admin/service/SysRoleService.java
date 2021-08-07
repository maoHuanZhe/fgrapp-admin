package com.fgrapp.admin.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysRoleMapper;
import com.fgrapp.admin.domain.SysRoleDo;
import com.fgrapp.admin.domain.SysRoleMenu;
import com.fgrapp.base.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SysRoleService
 *
 * @author fan guang rui
 * @date 2021年06月09日 21:51
 */
@Service
public class SysRoleService extends FgrService<SysRoleMapper, SysRoleDo> {
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRoleDo> perms = baseMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRoleDo perm : perms)
        {
            if (ObjectUtil.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    public List<SysRoleDo> getRoleList() {
        Long userId = FgrUtil.getUserId();
        if (FgrUtil.isAdmin(userId)){
            //如果是超级管理员 获取全部角色
            return baseMapper.selectList(new QueryWrapper<>());
        }
        //否则获取自己拥有的角色
        return baseMapper.selectRolePermissionByUserId(userId);
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysRoleDo.class),map);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleDo info) {
        if (checkRoleNameUnique(info)){
            throw new ResultException("新增角色'" + info.getRoleName() + "'失败，角色名称已存在");
        }
        if (checkRoleKeyUnique(info)){
            throw new ResultException("新增角色'" + info.getRoleName() + "'失败，角色权限已存在");
        }
        baseMapper.insert(info);
        //添加角色菜单管理
        List<SysRoleMenu> list = getSysRoleMenus(info);
        baseMapper.insetRoleMenuList(list);
    }

    private List<SysRoleMenu> getSysRoleMenus(SysRoleDo info) {
        List<SysRoleMenu> list = new ArrayList<>();
        Long id = info.getId();
        Long[] menuIds = info.getMenuIds();
        for (Long menuId : menuIds) {
            list.add(new SysRoleMenu(id,menuId));
        }
        return list;
    }

    private boolean checkRoleNameUnique(SysRoleDo info){
        List<SysRoleDo> list = baseMapper.selectList(new LambdaQueryWrapper<SysRoleDo>()
                .eq(SysRoleDo::getRoleName, info.getRoleName()));
        return FgrUtil.checkUnique(info,list);
    }
    private boolean checkRoleKeyUnique(SysRoleDo info){
        List<SysRoleDo> list = baseMapper.selectList(new LambdaQueryWrapper<SysRoleDo>()
                .eq(SysRoleDo::getRoleKey, info.getRoleKey()));
        return FgrUtil.checkUnique(info,list);
    }

    @Transactional(rollbackFor = Exception.class)
    public SysRoleDo edit(SysRoleDo info) {
        if (info.isAdmin()) {
            throw new ResultException("不允许操作超级管理员角色");
        }
        if (checkRoleNameUnique(info)){
            throw new ResultException("修改角色'" + info.getRoleName() + "'失败，角色名称已存在");
        }
        if (checkRoleKeyUnique(info)){
            throw new ResultException("修改角色'" + info.getRoleName() + "'失败，角色权限已存在");
        }
        baseMapper.updateById(info);
        //删除角色菜单关联
        baseMapper.deleteRoleMenuByRoleId(info.getId());
        //添加角色菜单关联
        List<SysRoleMenu> list = getSysRoleMenus(info);
        baseMapper.insetRoleMenuList(list);
        return info;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> roleIds) {
        List<Long> list = roleIds.stream().filter(FgrUtil::isAdmin).collect(Collectors.toList());
        if (list.size() > 0){
            throw new ResultException("不允许操作超级管理员角色");
        }
        int count = baseMapper.countUserRoleByRoleId(roleIds);
        if (count > 0){
            throw new ResultException("存在已分配角色,不能删除");
        }
        //删除角色与菜单关联
        baseMapper.deleteRoleMenu(roleIds);
        return baseMapper.deleteBatchIds(roleIds);
    }
}
