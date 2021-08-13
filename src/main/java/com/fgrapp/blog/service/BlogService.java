package com.fgrapp.blog.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.BlogClassMapper;
import com.fgrapp.blog.dao.BlogMapper;
import com.fgrapp.blog.dao.ClassMapper;
import com.fgrapp.blog.domain.BlogClassDo;
import com.fgrapp.blog.domain.BlogDo;
import com.fgrapp.blog.domain.ClassDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public BlogService(ClassMapper classMapper, BlogClassMapper blogClassMapper) {
        this.classMapper = classMapper;
        this.blogClassMapper = blogClassMapper;
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,BlogDo.class),map);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(BlogDo info) {
        //新增博客
        baseMapper.insert(info);
        Long blogId = info.getId();
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
        List<String> classNames = getClassNames(id);
        blogDo.setAddClassNames(classNames);
        return blogDo;
    }

    private List<String> getClassNames(Long id) {
        //获取博客分类信息
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
        baseMapper.updateById(info);
        List<String> classNames = getClassNames(blogId);
        info.setAddClassNames(classNames);
        return info;
    }
}
