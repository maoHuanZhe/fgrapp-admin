package com.fgrapp.topic.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.topic.domain.TopicCommentDo;
import com.fgrapp.topic.domain.TopicDo;
import com.fgrapp.topic.domain.TopicOperateNumDo;
import com.fgrapp.topic.service.TopicCommentService;
import com.fgrapp.topic.service.TopicService;
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
@RequestMapping("/page/topic")
@ResponseResultBody
@Api(tags = "前台页面控制器")
@CacheConfig(cacheNames = "topic")
public class TopicPageController extends FgrController {

    private final TopicService service;
    private final TopicCommentService commentService;

    public TopicPageController(TopicService service, TopicCommentService commentService) {
        this.service = service;
        this.commentService = commentService;
    }

    @GetMapping("list")
    @ApiOperation("获取问题分页数据(问题页面显示)")
    public Map<String,Object> page(@RequestParam Map<String,Object> map){
        return service.getTopicPage(map);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据编号获取问题详细信息")
    @Cacheable(unless = "#result == null")
    public TopicDo getInfo(@PathVariable Long id){
        return service.getInfo(id);
    }
    @GetMapping("/detail/{id}")
    @ApiOperation("根据编号获取问题详细信息")
    public Map<String,Object> getDetailInfo(@PathVariable Long id){
        return service.getDetailInfo(id);
    }


    @SaCheckLogin
    @PutMapping("/operateNum/{id}/{num}")
    @ApiOperation("问题点赞与取消点赞操作")
    public TopicOperateNumDo updateLickNum(@PathVariable Long id,@PathVariable int num){
        return service.updateLickNum(id,num);
    }
    @SaCheckLogin
    @PutMapping("/operateNum/collect/{id}/{num}")
    @ApiOperation("问题收藏与取消收藏操作")
    public TopicOperateNumDo updateCollectNum(@PathVariable Long id,@PathVariable int num){
        return service.updateCollectNum(id,num);
    }

    @PostMapping
    @SaCheckLogin
    @ApiOperation("新增问题评论")
    @Log(title = "问题评论信息", businessType = BusinessType.INSERT)
    @CacheEvict(cacheNames = "topicComment",key = "#info.topicId")
    public void save(@Validated @RequestBody TopicCommentDo info){
        commentService.save(info);
    }
}
