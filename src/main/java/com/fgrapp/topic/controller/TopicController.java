package com.fgrapp.topic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.BlogDo;
import com.fgrapp.topic.domain.TopicDo;
import com.fgrapp.topic.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TopicController
 *
 * @author fan guang rui
 * @date 2021年08月29日 9:24
 */
@RestController
@RequestMapping("func/topic")
@ResponseResultBody
@Api(tags = "问题控制器")
@CacheConfig(cacheNames = "topic")
public class TopicController extends FgrController {
    private final TopicService service;

    public TopicController(TopicService service) {
        this.service = service;
    }

    @GetMapping("page")
    @SaCheckPermission("func:topic:list")
    @ApiOperation("获取问题分页数据")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @PostMapping
    @ApiOperation("新增问题")
    @SaCheckPermission("func:topic:add")
    @Log(title = "问题信息", businessType = BusinessType.INSERT)
    public void save(@Validated @RequestBody TopicDo info){
        service.add(info);
    }

    @PutMapping
    @ApiOperation("修改问题")
    @SaCheckPermission("func:topic:edit")
    @CachePut(key = "#info.id")
    @Log(title = "问题信息", businessType = BusinessType.UPDATE)
    public TopicDo updeate(@Validated @RequestBody TopicDo info){
        return service.updeate(info);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除问题")
    @SaCheckPermission("func:topic:remove")
    @CacheEvict()
    @Log(title = "问题信息", businessType = BusinessType.DELETE)
    public void dels(@PathVariable Long id){
        service.dels(id);
    }
}
