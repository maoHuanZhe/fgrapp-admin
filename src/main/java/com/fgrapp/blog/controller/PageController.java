package com.fgrapp.blog.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.BlogDo;
import com.fgrapp.blog.domain.CommentDo;
import com.fgrapp.blog.service.BlogService;
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
 * PageController
 *
 * @author fan guang rui
 * @date 2021年07月31日 9:49
 */
@RestController
@RequestMapping("/page")
@ResponseResultBody
@Api(tags = "前台页面控制器")
@CacheConfig(cacheNames = "blog")
public class PageController extends FgrController {

    private final BlogService service;
    private final CommentService commentService;

    public PageController(BlogService service, CommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }

    @GetMapping("list")
    @ApiOperation("获取博客分页数据(博客页面显示)")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getBlogPage(map);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据编号获取博客详细信息")
    @Cacheable(unless = "#result == null")
    public BlogDo getInfo(@PathVariable Long id){
        return service.getInfo(id);
    }
    @GetMapping("/detail/{id}")
    @ApiOperation("根据编号获取博客详细信息")
    public Map<String,Object> getDetailInfo(@PathVariable Long id){
        return service.getDetailInfo(id);
    }


    @SaCheckLogin
    @PutMapping("/operateNum/{id}/{num}")
    @ApiOperation("博客点赞与取消点赞操作")
    public void updateLickNum(@PathVariable Long id,@PathVariable int num){
        service.updateLickNum(id,num);
    }

    @PostMapping
    @SaCheckLogin
    @ApiOperation("新增博客评论")
    @Log(title = "博客评论信息", businessType = BusinessType.INSERT)
    @CacheEvict(cacheNames = "comment",key = "#info.blogId")
    public void save(@Validated @RequestBody CommentDo info){
        commentService.save(info);
    }
}
