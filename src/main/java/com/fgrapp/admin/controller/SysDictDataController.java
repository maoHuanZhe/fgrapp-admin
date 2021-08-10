package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.domain.SysDictDataDo;
import com.fgrapp.admin.service.SysDictDataService;
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
 * SysDictDataController
 *
 * @author fan guang rui
 * @date 2021年08月05日 13:49
 */
@Api(tags="数据字典信息")
@RestController
@ResponseResultBody
@RequestMapping("/system/dict/data")
public class SysDictDataController extends FgrController {
    private final SysDictDataService service;

    public SysDictDataController(SysDictDataService service) {
        this.service = service;
    }

    @ApiOperation(value = "根据字典类型查询字典数据信息")
    @GetMapping("/type/{dictType}")
    @Cacheable(value = "dictTypeId")
    public List<SysDictDataDo> dictType(@PathVariable Long dictType){
        return service.dictType(dictType);
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/page")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }
    @ApiOperation(value = "查询字典数据详细")
    @SaCheckPermission("system:dict:query")
    @GetMapping("/{id}")
    @Cacheable(value = "dictData",unless="#result == null")
    public SysDictDataDo getInfo(@PathVariable Long id){
        return service.getById(id);
    }
    @ApiOperation(value = "新增字典数据")
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典数据管理", businessType = BusinessType.INSERT)
    @CacheEvict(value = "dictTypeId",key = "#info.dictType")
    @PostMapping
    public boolean save(@Validated @RequestBody SysDictDataDo info){
        return service.save(info);
    }
    @ApiOperation(value = "修改字典数据")
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典数据管理", businessType = BusinessType.UPDATE)
    @CachePut(value = "dictData",key = "#info.id",unless="#result == null")
    @CacheEvict(value = "dictTypeId",key = "#info.dictType")
    @PutMapping
    public SysDictDataDo edit(@RequestBody SysDictDataDo info){
        service.updateById(info);
        return info;
    }
    @ApiOperation(value = "删除字典数据")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典数据管理", businessType = BusinessType.DELETE)
    @CacheEvict(value = {"dictData","dictType"},allEntries = true)
    @DeleteMapping("/{ids}")
    public void dels(@PathVariable List<Long> ids){
        service.removeByIds(ids);
    }
}
