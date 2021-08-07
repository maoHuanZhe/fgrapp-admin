package com.fgrapp.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysRoleMapper;
import com.fgrapp.admin.dao.SysUserMapper;
import com.fgrapp.admin.domain.SysRoleDo;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.domain.SysUserRole;
import com.fgrapp.base.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SysUserService
 *
 * @author fan guang rui
 * @date 2021年08月01日 9:37
 */
@Service
public class SysUserService extends FgrService<SysUserMapper, SysUserDo> {

    @Value("${fgr.privateKey}")
    private String privateKey;

    @Autowired
    private SysRoleMapper roleMapper;

    public SysUserDo selectUserByUserName(String userName) {
        return baseMapper.selectUserByUserName(userName);
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysUserDo.class),map);
    }

    public SysUserDo getInfo(Long userId) {
        SysUserDo userDo = baseMapper.selectById(userId);
        //获取角色信息
        List<SysRoleDo> roleDos = roleMapper.selectRolePermissionByUserId(userId);
        Long[] roleIds = new Long[roleDos.size()];
        for (int i = 0; i < roleDos.size(); i++) {
            roleIds[i] = roleDos.get(i).getId();
        }
        userDo.setRoleIds(roleIds);
        return userDo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserDo user) {
        if (checkUserNameUnique(user)){
            throw new ResultException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        if (StrUtil.isNotEmpty(user.getPhonenumber()) && checkPhoneUnique(user)){
            throw new ResultException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StrUtil.isNotEmpty(user.getEmail()) && checkEmailUnique(user)){
            throw new ResultException("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(FgrUtil.encryptionPassword(user.getPassword(), privateKey));
        //新增用户
        baseMapper.insert(user);
        List<SysUserRole> list = getSysUserRoles(user);
        //新增用户与角色关联
        baseMapper.insertUserRoleList(list);
    }

    public List<SysUserRole> getSysUserRoles(SysUserDo user) {
        Long userId = user.getId();
        List<SysUserRole> list = new ArrayList<>();
        Long[] roleIds = user.getRoleIds();
        for (Long roleId : roleIds) {
            list.add(new SysUserRole(userId,roleId));
        }
        return list;
    }

    private boolean checkUserNameUnique(SysUserDo info) {
        List<SysUserDo> list = baseMapper.selectList(new LambdaQueryWrapper<SysUserDo>()
                .eq(SysUserDo::getUserName, info.getUserName()));
        return FgrUtil.checkUnique(info, list);
    }
    private boolean checkPhoneUnique(SysUserDo info) {
        List<SysUserDo> list = baseMapper.selectList(new LambdaQueryWrapper<SysUserDo>()
                .eq(SysUserDo::getPhonenumber, info.getPhonenumber()));
        return FgrUtil.checkUnique(info, list);
    }
    private boolean checkEmailUnique(SysUserDo info) {
        List<SysUserDo> list = baseMapper.selectList(new LambdaQueryWrapper<SysUserDo>()
                .eq(SysUserDo::getEmail, info.getEmail()));
        return FgrUtil.checkUnique(info, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public SysUserDo edit(SysUserDo user) {
        if (user.isAdmin()) {
            throw new ResultException("不允许操作超级管理员用户");
        }
        if (StrUtil.isNotEmpty(user.getPhonenumber()) && checkPhoneUnique(user)){
                throw new ResultException("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
            }
        if (StrUtil.isNotEmpty(user.getEmail()) && checkEmailUnique(user)){
            throw new ResultException("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        baseMapper.updateById(user);
        //删除用户与角色关联
        baseMapper.deleteUserRoleList(user.getId());
        //新增用户与角色关联
        List<SysUserRole> list = getSysUserRoles(user);
        baseMapper.insertUserRoleList(list);
        return user;
    }

    public int deleteByIds(List<Long> userIds) {
        List<Long> list = userIds.stream().filter(FgrUtil::isAdmin).collect(Collectors.toList());
        if (list.size() > 0){
            throw new ResultException("不允许操作超级管理员用户");
        }
        //删除用户与角色关联
        baseMapper.deleteUserRoleListByIds(userIds);
        return baseMapper.deleteBatchIds(userIds);
    }

    public int resetPwd(SysUserDo user) {
        if (user.isAdmin()) {
            throw new ResultException("不允许操作超级管理员用户");
        }
        user.setPassword(FgrUtil.encryptionPassword(user.getPassword(), privateKey));
        return baseMapper.updateById(user);
    }

    public int changeStatus(SysUserDo user) {
        if (user.isAdmin()) {
            throw new ResultException("不允许操作超级管理员用户");
        }
        return baseMapper.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertAuthRole(SysUserDo user) {
        if (user.isAdmin()) {
            throw new ResultException("不允许操作超级管理员用户");
        }
        baseMapper.updateById(user);
        //删除用户与角色关联
        baseMapper.deleteUserRoleList(user.getId());
        //新增用户与角色关联
        List<SysUserRole> list = getSysUserRoles(user);
        baseMapper.insertUserRoleList(list);
    }
}
