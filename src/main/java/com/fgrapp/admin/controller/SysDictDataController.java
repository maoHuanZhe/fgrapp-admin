package com.fgrapp.admin.controller;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fgrapp.admin.domain.SysDictDataDo;
import com.fgrapp.admin.service.SysDictDataService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@CacheConfig(cacheNames = "dictData")
public class SysDictDataController extends FgrController {
    @Autowired
    private SysDictDataService service;
    @ApiOperation(value = "根据字典类型查询字典数据信息",notes = "根据字典类型查询字典数据信息")
    @GetMapping("/type/{dictType}")
    @Cacheable(value = "dictType")
    public List<SysDictDataDo> dictType(@PathVariable String dictType){
        return service.dictType(dictType);
    }

}
