package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.domain.SysDictTypeDo;
import com.fgrapp.admin.service.SysDictTypeService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SysDictTypeConroller
 *
 * @author fan guang rui
 * @date 2021年08月10日 12:37
 */
@Api(tags="数据字典类型信息")
@RestController
@ResponseResultBody
@RequestMapping("/system/dict/type")
public class SysDictTypeConroller extends FgrController {
    private final SysDictTypeService service;

    public SysDictTypeConroller(SysDictTypeService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/page")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }
    @ApiOperation(value = "查询全部字典类型列表")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public List<SysDictTypeDo> page(){
        return service.list();
    }
    @ApiOperation(value = "查询字典类型详细")
    @SaCheckPermission("system:dict:query")
    @GetMapping("/{dictId}")
    @Cacheable(value = "dictType",unless="#result == null")
    public SysDictTypeDo getInfo(@PathVariable Long dictId){
        return service.getById(dictId);
    }
    @ApiOperation(value = "新增数据字典类型")
    @SaCheckPermission("system:dict:add")
    @Log(title = "数据字典类型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public int save(@Validated @RequestBody SysDictTypeDo info){
        return service.add(info);
    }
    @ApiOperation(value = "修改数据字典类型")
    @SaCheckPermission("system:dict:edit")
    @Log(title = "数据字典类型管理", businessType = BusinessType.UPDATE)
    @CachePut(value = "dictType",key = "#info.id",unless="#result == null")
    @PutMapping
    public SysDictTypeDo edit(@Validated @RequestBody SysDictTypeDo info){
        return service.edit(info);
    }
    @ApiOperation(value = "删除数据字典类型")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "数据字典类型管理", businessType = BusinessType.DELETE)
    @CacheEvict(value = "dictType",allEntries = true)
    @DeleteMapping("/{dictTypeIds}")
    public void dels(@PathVariable List<Long> dictTypeIds){
        service.dels(dictTypeIds);
    }

}
