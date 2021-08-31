package com.fgrapp.topic.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.topic.domain.TopicCommentDo;
import com.fgrapp.topic.service.TopicCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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
@RequestMapping("func/topicComment")
@ResponseResultBody
@Api(tags = "问题评论控制器")
@CacheConfig(cacheNames = "topicComment")
public class TopicCommentController extends FgrController {
    private final TopicCommentService service;

    public TopicCommentController(TopicCommentService service) {
        this.service = service;
    }
    @GetMapping("page")
    @SaCheckPermission("func:topicComment:list")
    @ApiOperation("获取问题评论分页数据")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @PutMapping
    @ApiOperation("修改问题评论审核状态")
    @SaCheckPermission("func:topicComment:edit")
    @CacheEvict(key = "#info.topicId")
    @Log(title = "问题评论信息", businessType = BusinessType.UPDATE)
    public void updeate(@Validated @RequestBody TopicCommentDo info){
        service.updateById(info);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除问题评论")
    @SaCheckPermission("func:topicComment:remove")
    @Log(title = "问题评论信息", businessType = BusinessType.DELETE)
    public void dels(@PathVariable List<Long> ids){
        service.dels(ids);
    }
}
