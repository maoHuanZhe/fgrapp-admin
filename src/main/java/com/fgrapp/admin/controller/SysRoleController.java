package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.domain.SysRoleDo;
import com.fgrapp.admin.domain.SysUserRole;
import com.fgrapp.admin.service.SysRoleService;
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
 * SysRoleController
 *
 * @author fan guang rui
 * @date 2021年08月07日 14:39
 */
@RestController
@RequestMapping("/system/role")
@ResponseResultBody
@Api(tags = "角色控制器")
public class SysRoleController extends FgrController {
    private final SysRoleService service;

    public SysRoleController(SysRoleService service) {
        this.service = service;
    }

    @ApiOperation(value = "获取当前登陆用户的角色列表")
    @SaCheckLogin
    @GetMapping("/roleList")
    public List<SysRoleDo> getRoleList(){
        return service.getRoleList();
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:role:list")
    @GetMapping("/page")
    public IPage<List<Map<String, Object>>> list(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @ApiOperation(value = "根据角色编号获取详细信息")
    @SaCheckPermission("system:role:query")
    @GetMapping(value ="/{id}")
    @Cacheable(value = "roleId",unless="#result == null")
    public SysRoleDo getInfo(@PathVariable Long id){
        return service.getById(id);
    }
    @ApiOperation(value = "新增角色")
    @SaCheckPermission("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysRoleDo info){
        service.add(info);
    }
    @ApiOperation(value = "修改角色")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @CachePut(value = "roleId",key = "#info.id",unless="#result == null")
    @PutMapping
    public SysRoleDo edit(@Validated @RequestBody SysRoleDo info){
        return service.edit(info);
    }
    @ApiOperation(value = "状态修改")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @CacheEvict(value = "roleId",allEntries = true)
    @PutMapping("/changeStatus")
    public int changeStatus(@RequestBody SysRoleDo info){
        return service.changeStatus(info);
    }
    @ApiOperation(value = "批量删除角色信息")
    @SaCheckPermission("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    @CacheEvict(value = "roleId",allEntries = true)
    public int remove(@PathVariable List<Long> roleIds){
        return service.deleteByIds(roleIds);
    }


    @ApiOperation(value = "查询已分配用户角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/allocatedList")
    public IPage<List<Map<String, Object>>> allocatedList(@RequestParam Map<String,Object> map){
        return service.allocatedList(map);
    }
    @ApiOperation(value = "查询未分配用户角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    public IPage<List<Map<String, Object>>> unallocatedList(@RequestParam Map<String,Object> map){
        return service.unallocatedList(map);
    }
    @ApiOperation(value = "取消授权用户")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @SaCheckPermission("system:role:edit")
    @PutMapping("/authUser/cancel")
    public int cancelAuthUser(@RequestBody SysUserRole userRole){
        return service.cancelAuthUser(userRole);
    }
    @ApiOperation(value = "批量取消授权用户")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @SaCheckPermission("system:role:d")
    @DeleteMapping("/authUser/{roleId}/{userIds}")
    public int cancelAuthUserAll(@PathVariable("roleId") Long roleId, @PathVariable("userIds") List<Long> userIds){
        return service.cancelAuthUserAll(roleId,userIds);
    }
    @ApiOperation(value = "批量选择用户授权")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @SaCheckPermission("system:role:remove")
    @PutMapping("/authUser/{roleId}/{userIds}")
    public void selectAuthUserAll(@PathVariable("roleId") Long roleId, @PathVariable("userIds") List<Long> userIds){
        service.selectAuthUserAll(roleId,userIds);
    }

}
