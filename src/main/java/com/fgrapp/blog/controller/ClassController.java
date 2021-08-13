package com.fgrapp.blog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.ClassDo;
import com.fgrapp.blog.service.ClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassController
 *
 * @author fan guang rui
 * @date 2021年08月12日 12:43
 */
@RestController
@RequestMapping("func/class")
@ResponseResultBody
@Api(tags = "博客分类控制器")
@CacheConfig(cacheNames = "class")
public class ClassController extends FgrController {
    private final ClassService service;

    public ClassController(ClassService service) {
        this.service = service;
    }

    @GetMapping("page")
    @SaCheckPermission("func:class:list")
    @ApiOperation("获取博客分类分页数据")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @GetMapping("list")
    @SaCheckPermission("func:class:list")
    @ApiOperation("获取全部博客分类列表数据")
    public List<ClassDo> list(){
        return service.list();
    }

    @GetMapping("/{id}")
    @SaCheckPermission("func:class:query")
    @ApiOperation("根据编号获取博客分类详细信息")
    @Cacheable(unless = "#result == null")
    public ClassDo getInfo(@PathVariable Long id){
        return service.getById(id);
    }
    @PostMapping
    @ApiOperation("新增博客分类")
    @SaCheckPermission("func:class:add")
    @Log(title = "博客分类信息", businessType = BusinessType.INSERT)
    public ClassDo save(@Validated @RequestBody ClassDo info){
        service.save(info);
        return info;
    }

    @PutMapping
    @ApiOperation("修改博客分类")
    @SaCheckPermission("func:class:edit")
    @CachePut(key = "#info.id")
    @Log(title = "博客分类信息", businessType = BusinessType.UPDATE)
    public ClassDo updeate(@Validated @RequestBody ClassDo info){
        service.updateById(info);
        return info;
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除博客分类")
    @SaCheckPermission("func:class:remove")
    @CacheEvict()
    @Log(title = "博客分类信息", businessType = BusinessType.DELETE)
    public void dels(@PathVariable List<Long> ids){
        service.dels(ids);
    }
}
