package com.fgrapp.weChat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.BlogCollectUserMapper;
import com.fgrapp.blog.dao.BlogMapper;
import com.fgrapp.blog.dao.BlogUserMapper;
import com.fgrapp.blog.dao.CommentMapper;
import com.fgrapp.blog.domain.BlogCollectUserDo;
import com.fgrapp.blog.domain.BlogDo;
import com.fgrapp.blog.domain.BlogUserDo;
import com.fgrapp.blog.domain.CommentDo;
import com.fgrapp.topic.dao.TopicCollectUserMapper;
import com.fgrapp.topic.dao.TopicCommentMapper;
import com.fgrapp.topic.dao.TopicLikeUserMapper;
import com.fgrapp.topic.domain.TopicCollectUserDo;
import com.fgrapp.topic.domain.TopicCommentDo;
import com.fgrapp.topic.domain.TopicLikeUserDo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WeChatAboutService
 *
 * @author fan guang rui
 * @date 2021年09月09日 17:37
 */
@Service
public class WeChatAboutService {

    private final CommentMapper blogCommentMapper;
    private final BlogUserMapper blogUserMapper;
    private final TopicCommentMapper topicCommentMapper;
    private final TopicLikeUserMapper topicLikeUserMapper;
    private final TopicCollectUserMapper topicCollectUserMapper;
    private final BlogMapper blogMapper;
    private final BlogCollectUserMapper blogCollectUserMapper;

    public WeChatAboutService(CommentMapper blogCommentMapper, BlogUserMapper blogUserMapper, TopicCommentMapper topicCommentMapper, TopicLikeUserMapper topicLikeUserMapper, TopicCollectUserMapper topicCollectUserMapper, BlogMapper blogMapper, BlogCollectUserMapper blogCollectUserMapper) {
        this.blogCommentMapper = blogCommentMapper;
        this.blogUserMapper = blogUserMapper;
        this.topicCommentMapper = topicCommentMapper;
        this.topicLikeUserMapper = topicLikeUserMapper;
        this.topicCollectUserMapper = topicCollectUserMapper;
        this.blogMapper = blogMapper;
        this.blogCollectUserMapper = blogCollectUserMapper;
    }

    /**
     * 获取用户 操作数据 点赞数 评论数 收藏数
     * 如果是管理员用户 获取待审核的评论
     * @return map
     * @param roles roles
     */
    public Map<String, Object> getOperateNum(Object roles) {
        Map<String,Object> map = new HashMap<>(5);
        Integer topicCollectNum = 0;
        Integer topicLikeNum = 0;
        Integer topicCommentNum = 0;
        Integer blogLikeNum = 0;
        Integer blogCollectNum = 0;
        Integer blogCommentNum = 0;
        List<TopicCommentDo> topicCommentDos = new ArrayList<>();
        List<CommentDo> blogComments = new ArrayList<>();
        Long userId = FgrUtil.getUserId();
        if (userId!= -1){
            String username = FgrUtil.getUsername();
            //用户已登录
            //判断用户是否管理员角色
            if (FgrUtil.isAdmin(roles)){
                //是管理员角色
                //获取全部评论博客数
                blogCommentNum = blogCommentMapper.selectCount(new LambdaQueryWrapper<>());
                //获取全部点赞博客数
                blogLikeNum = blogUserMapper.selectCount(new LambdaQueryWrapper<>());
                blogCollectNum = blogCollectUserMapper.selectCount(new LambdaQueryWrapper<>());
                //获取全部评论题库数
                topicCommentNum = topicCommentMapper.selectCount(new LambdaQueryWrapper<>());
                //获取全部点赞题目数
                topicLikeNum = topicLikeUserMapper.selectCount(new LambdaQueryWrapper<>());
                //获取全部收藏题目数
                topicCollectNum = topicCollectUserMapper.selectCount(new LambdaQueryWrapper<>());
                //获取待审批评论列表
                blogComments = blogCommentMapper.selectList(new LambdaQueryWrapper<CommentDo>()
                        .eq(CommentDo::getIsAudit, 0));
                topicCommentDos = topicCommentMapper.selectList(new LambdaQueryWrapper<TopicCommentDo>()
                        .eq(TopicCommentDo::getIsAudit, 0));
            } else {
                //普通用户
                //获取用户评论博客数
                blogCommentNum = blogCommentMapper.selectCount(new LambdaQueryWrapper<CommentDo>()
                        .eq(CommentDo::getCreateAt, username));
                //获取用户点赞博客数
                blogLikeNum = blogUserMapper.selectCount(new LambdaQueryWrapper<BlogUserDo>()
                        .eq(BlogUserDo::getUserId, userId));
                blogCollectNum = blogCollectUserMapper.selectCount((new LambdaQueryWrapper<BlogCollectUserDo>()
                .eq(BlogCollectUserDo::getUserId,userId)));
                //获取用户评论题库数
                topicCommentNum = topicCommentMapper.selectCount(new LambdaQueryWrapper<TopicCommentDo>()
                        .eq(TopicCommentDo::getCreateAt, username));
                //获取用户点赞题目数
                topicLikeNum = topicLikeUserMapper.selectCount(new LambdaQueryWrapper<TopicLikeUserDo>()
                        .eq(TopicLikeUserDo::getUserId, userId));
                //获取用户收藏题目数
                topicCollectNum = topicCollectUserMapper.selectCount(new LambdaQueryWrapper<TopicCollectUserDo>()
                        .eq(TopicCollectUserDo::getUserId, userId));
            }
        }
        map.put("blogComments",blogComments);
        map.put("topicCommentDos",topicCommentDos);
        map.put("likeNum",blogLikeNum+topicLikeNum);
        map.put("commentNum",blogCommentNum+topicCommentNum);
        map.put("collect",topicCollectNum+blogCollectNum);
        return map;
    }

    public IPage<List<Map<String, Object>>> search(Map<String, Object> map) {
        return blogMapper.search(PageUtil.getParamPage(map, BlogDo.class),map);
    }
}
