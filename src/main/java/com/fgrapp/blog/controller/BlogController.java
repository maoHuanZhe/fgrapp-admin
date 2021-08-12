package com.fgrapp.blog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.BlogDo;
import com.fgrapp.blog.service.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BlogController
 *
 * @author fan guang rui
 * @date 2021年07月31日 9:49
 */
@RestController
@RequestMapping("func/blog")
@ResponseResultBody
@Api(tags = "博客控制器")
@CacheConfig(cacheNames = "blog")
public class BlogController extends FgrController {

    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    @GetMapping("page")
    @SaCheckPermission("func:blog:list")
    @ApiOperation("获取博客分页数据")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @GetMapping("/{id}")
    @SaCheckPermission("func:blog:query")
    @ApiOperation("根据编号获取博客详细信息")
    @Cacheable(unless = "#result == null")
    public BlogDo getInfo(@PathVariable Long id){
        return service.getById(id);
    }
    @PostMapping
    @ApiOperation("新增博客")
    @SaCheckPermission("func:blog:add")
    @Log(title = "博客信息", businessType = BusinessType.INSERT)
    public boolean save(@Validated @RequestBody BlogDo info){
        return service.save(info);
    }

    @PutMapping
    @ApiOperation("修改博客")
    @SaCheckPermission("func:blog:edit")
    @CachePut(key = "#info.id")
    @Log(title = "博客信息", businessType = BusinessType.UPDATE)
    public BlogDo updeate(@Validated @RequestBody BlogDo info){
        service.updateById(info);
        return info;
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除博客")
    @SaCheckPermission("func:blog:remove")
    @CacheEvict()
    @Log(title = "博客信息", businessType = BusinessType.DELETE)
    public void dels(@PathVariable Long id){
        service.removeById(id);
    }
}
