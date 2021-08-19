package com.fgrapp.blog.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.config.redis.RedisCache;
import com.fgrapp.base.constant.Constants;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.*;
import com.fgrapp.blog.domain.*;
import com.fgrapp.blog.domain.vo.RegisterVo;
import com.fgrapp.blog.util.MDToText;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * BlogService
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:19
 */
@Slf4j
@Service
public class BlogService extends FgrService<BlogMapper, BlogDo> {

    private final ClassMapper classMapper;
    private final BlogClassMapper blogClassMapper;
    private final BlogOperateNumMapper operateNumMapper;
    private final CommentMapper commentMapper;
    private final BlogUserMapper blogUserMapper;

    public BlogService(ClassMapper classMapper, BlogClassMapper blogClassMapper, BlogOperateNumMapper operateNumMapper, CommentMapper commentMapper, BlogUserMapper blogUserMapper) {
        this.classMapper = classMapper;
        this.blogClassMapper = blogClassMapper;
        this.operateNumMapper = operateNumMapper;
        this.blogUserMapper = blogUserMapper;
        this.commentMapper = commentMapper;
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,BlogDo.class),map);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(BlogDo info) {
        //获取博客摘要
        String summary = MDToText.mdToText(info.getContent()).substring(0, 128);
        info.setSummary(summary);
        //新增博客
        baseMapper.insert(info);
        Long blogId = info.getId();
        //新增博客操作数据
        operateNumMapper.insert(BlogOperateNumDo.builder()
                .blogId(blogId).likeNum(0L).readNum(0L).build());
        List<BlogClassDo> blogClassDos = new ArrayList<>();
        //判断是否有需要添加的分类
        List<String> addClassNames = info.getAddClassNames();
        if (ObjectUtil.isNotNull(addClassNames)){
            log.info("存在需要新增的分类");
            addClassNames.forEach(className -> {
                log.info("新增博客分类:{}",className);
                ClassDo classDo = ClassDo.builder().name(className).build();
                classMapper.insert(classDo);
                blogClassDos.add(new BlogClassDo(blogId,classDo.getId()));
            });
        }
        //添加博客与分类关联
        List<Long> classIds = info.getClassIds();
        if (ObjectUtil.isNotNull(classIds)){
            log.info("添加已存在的分类与博客关联");
            classIds.forEach(classId -> blogClassDos.add(new BlogClassDo(blogId,classId)));
        }
        if (blogClassDos.size() > 0){
            int allBatch = blogClassMapper.insertBatch(blogClassDos);
            log.info("新增{}条博客分类关联",allBatch);
        }
    }

    public BlogDo getInfo(Long id) {
        //获取博客基本信息
        BlogDo blogDo = baseMapper.selectById(id);
        //获取博客分类信息
        List<String> classNames = getClassNames(id);
        blogDo.setAddClassNames(classNames);
        return blogDo;
    }

    private List<String> getClassNames(Long id) {
        List<ClassDo> classDoList = classMapper.getListByBlogId(id);
        List<String> classNames = new ArrayList<>();
        classDoList.forEach(item ->{
        classNames.add(item.getName());
        });
        return classNames;
    }

    @Transactional(rollbackFor = Exception.class)
    public BlogDo updeate(BlogDo info) {
        Long blogId = info.getId();
        int delete = blogClassMapper.delete(new LambdaUpdateWrapper<BlogClassDo>()
                .eq(BlogClassDo::getBlogId, blogId));
        log.info("删除原有博客分类信息,删除的关联数量：{}",delete);
        List<BlogClassDo> blogClassDos = new ArrayList<>();
        //判断是否有需要添加的分类
        List<String> addClassNames = info.getAddClassNames();
        if (ObjectUtil.isNotNull(addClassNames)){
            log.info("存在需要新增的分类");
            addClassNames.forEach(className -> {
                log.info("新增博客分类:{}",className);
                ClassDo classDo = ClassDo.builder().name(className).build();
                classMapper.insert(classDo);
                blogClassDos.add(new BlogClassDo(blogId,classDo.getId()));
            });
        }
        //添加博客与分类关联
        List<Long> classIds = info.getClassIds();
        if (ObjectUtil.isNotNull(classIds)){
            log.info("添加已存在的分类与博客关联");
            classIds.forEach(classId -> blogClassDos.add(new BlogClassDo(blogId,classId)));
        }
        if (blogClassDos.size() > 0){
            int allBatch = blogClassMapper.insertBatch(blogClassDos);
            log.info("新增{}条博客分类关联",allBatch);
        }
        //获取博客摘要
        String summary = MDToText.mdToText(info.getContent()).substring(0, 128);
        info.setSummary(summary);
        baseMapper.updateById(info);
        List<String> classNames = getClassNames(blogId);
        info.setAddClassNames(classNames);
        return info;
    }

    public IPage<List<Map<String, Object>>> getBlogPage(Map<String, Object> map) {
        return baseMapper.getBlogPage(PageUtil.getParamPage(map,BlogDo.class),map);
    }

    public void dels(Long id) {
        baseMapper.dels(id);
    }

    public BlogOperateNumDo getOperateNum(Long id) {
        //博客阅读数加一
        operateNumMapper.updateReadNum(id);
        BlogOperateNumDo blogOperateNumDo = operateNumMapper.selectById(id);
        //判断当前用户能否点赞
        if (!StpUtil.isLogin()){
            //未登陆
            blogOperateNumDo.setCanLike(true);
        } else {
            Long userId = FgrUtil.getUserId();
            Integer count = blogUserMapper.selectCount(new LambdaQueryWrapper<BlogUserDo>()
                    .eq(BlogUserDo::getBlogId, id)
                    .eq(BlogUserDo::getUserId, userId));
            blogOperateNumDo.setCanLike(count == 0);
        }
        return blogOperateNumDo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateLickNum(Long id, int num) {
        //点赞数量改变
        Long userId = FgrUtil.getUserId();
        operateNumMapper.updateLickNum(id,num);
        if (num > 0){
            //点赞操作
            //添加博客与用户点赞关联
            blogUserMapper.insert(new BlogUserDo(id,userId));
        } else {
            //取消点赞操作
            //删除博客与用户点赞关联
            blogUserMapper.delete(new LambdaUpdateWrapper<BlogUserDo>()
            .eq(BlogUserDo::getUserId,userId)
            .eq(BlogUserDo::getBlogId,id));
        }
    }

    public Map<String, Object> getDetailInfo(Long id) {
        Map<String,Object> map = new HashMap<>(3);
        BlogDo blogDo = getInfo(id);
        BlogOperateNumDo operateNum = getOperateNum(id);
        List<CommentDo> commentDos = commentMapper.selectList(new LambdaQueryWrapper<CommentDo>()
                .eq(CommentDo::getBlogId, id)
                .eq(CommentDo::getIsAudit, 1)
                .orderByDesc(CommentDo::getCreateTime));
        map.put("blog",blogDo);
        map.put("operateNum",operateNum);
        map.put("commentDos",commentDos);
        return map;
    }
}
