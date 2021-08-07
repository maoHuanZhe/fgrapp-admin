package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.service.SysUserService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SysUserController
 *
 * @author fan guang rui
 * @date 2021年08月06日 20:37
 */
@RestController
@RequestMapping("/system/user")
@ResponseResultBody
@Api(tags = "用户控制器")
public class SysUserController extends FgrController {
    private final SysUserService service;

    public SysUserController(SysUserService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:user:list")
    @GetMapping("/page")
    public IPage<List<Map<String, Object>>> list(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @ApiOperation(value = "根据用户编号获取详细信息")
    @SaCheckPermission("system:user:query")
    @GetMapping(value ="/{userId}")
    @Cacheable(value = "userId",unless="#result == null")
    public SysUserDo getInfo(@PathVariable Long userId){
        return service.getInfo(userId);
    }
    @ApiOperation(value = "新增用户")
    @SaCheckPermission("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysUserDo user){
        service.add(user);
    }
    @ApiOperation(value = "修改用户")
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @CachePut(value = "userId",key = "#user.id",unless="#result == null")
    @PutMapping
    public SysUserDo edit(@Validated @RequestBody SysUserDo user){
        return service.edit(user);
    }
    @ApiOperation(value = "批量删除用户信息")
    @SaCheckPermission("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    @CacheEvict(value = "userId",allEntries = true)
    public int remove(@PathVariable List<Long> userIds){
        return service.deleteByIds(userIds);
    }
    @ApiOperation(value = "重置密码")
    @SaCheckPermission("system:user:resetPwd")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public int resetPwd(@RequestBody SysUserDo user){
        return service.resetPwd(user);
    }

    @ApiOperation(value = "状态修改")
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @CacheEvict(value = "userId",allEntries = true)
    public int changeStatus(@RequestBody SysUserDo user){
        return service.changeStatus(user);
    }
    @ApiOperation(value = "用户授权角色")
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    @CacheEvict(value = "userId",allEntries = true)
    public void insertAuthRole(@RequestBody SysUserDo user){
        service.insertAuthRole(user);
    }
}
