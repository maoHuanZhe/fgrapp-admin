package com.fgrapp.blog.controller;

import cn.hutool.core.date.DateTime;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.BlogDo;
import com.fgrapp.blog.service.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("blog")
@ResponseResultBody
@Api(tags = "博客控制器")
public class BlogController extends FgrController {

    @Autowired
    private BlogService service;

    @GetMapping("list")
    @ApiOperation(value = "获取列表")
    public List<Map<String, Object>> list(){
        return service.listMaps();
    }

    @PostMapping
    @ApiOperation(value = "新增博客")
    @Log(title = "博客信息", businessType = BusinessType.INSERT)
    public boolean save(@RequestBody BlogDo info){
        return service.save(info);
    }

    @PutMapping
    @ApiOperation(value = "修改博客")
    @Log(title = "博客信息", businessType = BusinessType.UPDATE)
    public boolean updeate(@RequestBody BlogDo info){
        return service.updateById(info);
    }
}
