package com.fgrapp.blog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.CommentDo;
import com.fgrapp.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CommentController
 *
 * @author fan guang rui
 * @date 2021年08月13日 21:14
 */
@RestController
@RequestMapping("func/comment")
@ResponseResultBody
@Api(tags = "博客评论控制器")
@CacheConfig(cacheNames = "comment")
public class CommentController extends FgrController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }
    @GetMapping("page")
    @SaCheckPermission("func:comment:list")
    @ApiOperation("获取博客评论分页数据")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @PutMapping
    @ApiOperation("修改博客评论审核状态")
    @SaCheckPermission("func:comment:edit")
    @CacheEvict(key = "#info.blogId")
    @Log(title = "博客评论信息", businessType = BusinessType.UPDATE)
    public void updeate(@Validated @RequestBody CommentDo info){
        service.updateById(info);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除博客评论")
    @SaCheckPermission("func:comment:remove")
    @Log(title = "博客评论信息", businessType = BusinessType.DELETE)
    public void dels(@PathVariable List<Long> ids){
        service.dels(ids);
    }
}
