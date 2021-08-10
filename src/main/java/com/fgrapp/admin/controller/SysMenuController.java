package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fgrapp.admin.domain.SysMenuDo;
import com.fgrapp.admin.domain.vo.TreeSelectVo;
import com.fgrapp.admin.service.SysMenuService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.base.utils.FgrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SysMenuController
 *
 * @author fan guang rui
 * @date 2021年07月31日 15:30
 */
@RestController
@RequestMapping("/system/menu")
@ResponseResultBody
@Api(tags = "菜单控制器")
public class SysMenuController extends FgrController {

    private final SysMenuService service;

    public SysMenuController(SysMenuService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:menu:list")
    @GetMapping("/list")
    public List<SysMenuDo> list(SysMenuDo info){
        return service.list(info,FgrUtil.getUserId());
    }
    @ApiOperation(value = "根据菜单编号获取详细信息")
    @SaCheckPermission("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public SysMenuDo getInfo(@PathVariable Long menuId){
        return service.getById(menuId);
    }
    @ApiOperation(value = "新增菜单")
    @SaCheckPermission("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public int add(@Validated @RequestBody SysMenuDo menu){
        return service.add(menu);
    }
    @ApiOperation(value = "修改菜单")
    @SaCheckPermission("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public int edit(@Validated @RequestBody SysMenuDo menu){
        return service.edit(menu);
    }
    @ApiOperation(value = "删除菜单")
    @SaCheckPermission("system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public int remove(@PathVariable("menuId") Long menuId){
        return service.delete(menuId);
    }
    @ApiOperation(value = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public List<TreeSelectVo> treeselect(SysMenuDo menu){
        return service.treeselect(menu);
    }
    @ApiOperation(value = "获取角色对应菜单列表")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public List<Integer> roleMenuTreeselect(@PathVariable("roleId") Long roleId){
        return service.selectMenuListByRoleId(roleId);
    }

}
