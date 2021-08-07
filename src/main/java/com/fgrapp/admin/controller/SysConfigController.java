package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.domain.SysConfigDo;
import com.fgrapp.admin.service.SysConfigService;
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
 * SysConfigController
 *
 * @author fan guang rui
 * @date 2021年08月06日 21:28
 */

@RestController
@RequestMapping("/system/config")
@ResponseResultBody
@Api(tags = "参数控制器")
public class SysConfigController extends FgrController {
    private final SysConfigService service;

    public SysConfigController(SysConfigService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:config:list")
    @GetMapping("/page")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @ApiOperation(value = "根据参数编号获取详细信息")
    @SaCheckPermission("system:config:query")
    @GetMapping(value = "/{configId}")
    @Cacheable(value = "configId",unless="#result == null")
    public SysConfigDo getInfo(@PathVariable Long configId){
        return service.getById(configId);
    }
    @ApiOperation(value = "根据参数键名查询参数值")
    @GetMapping(value = "/configKey/{configKey}")
    @Cacheable(value = "configKey")
    public String getConfigKey(@PathVariable String configKey){
        return service.getConfigKey(configKey);
    }
    @ApiOperation(value = "新增参数配置")
    @SaCheckPermission("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public int add(@Validated @RequestBody SysConfigDo info){
        return service.add(info);
    }
    @ApiOperation(value = "修改参数配置")
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @CachePut(value = "configId",key = "#info.id",unless="#result == null")
    @PutMapping
    public SysConfigDo edit(@Validated @RequestBody SysConfigDo info){
        return service.edit(info);
    }
    @ApiOperation(value = "批量删除参数信息")
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    @CacheEvict(value = {"configKey","configId"},allEntries = true)
    public int remove(@PathVariable List<Long> configIds){
        return service.deleteConfigByIds(configIds);
    }

}
