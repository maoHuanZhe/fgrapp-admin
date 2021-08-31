package com.fgrapp.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.BlogClassMapper;
import com.fgrapp.blog.dao.ClassMapper;
import com.fgrapp.blog.domain.BlogClassDo;
import com.fgrapp.blog.domain.ClassDo;
import com.fgrapp.topic.dao.TopicClassMapper;
import com.fgrapp.topic.domain.TopicClassDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ClassService
 *
 * @author fan guang rui
 * @date 2021年08月12日 12:42
 */
@Service
public class ClassService extends FgrService<ClassMapper, ClassDo> {

    private final BlogClassMapper blogClassMapper;
    private final TopicClassMapper topicClassMapper;
    public ClassService(BlogClassMapper blogClassMapper, TopicClassMapper topicClassMapper) {
        this.blogClassMapper = blogClassMapper;
        this.topicClassMapper = topicClassMapper;
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,ClassDo.class),map);
    }

    public void dels(List<Long> ids) {
        //判断分类下是否有博客
        Integer count = blogClassMapper.selectCount(new LambdaQueryWrapper<BlogClassDo>()
                .in(BlogClassDo::getClassId, ids));
        if (count > 0){
            throw new ResultException("分类下存在博客，不能删除");
        }
        baseMapper.deleteBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void blogSort(List<BlogClassDo> list) {
        Long classId = list.get(0).getClassId();
        blogClassMapper.delete(new LambdaQueryWrapper<BlogClassDo>().eq(BlogClassDo::getClassId,classId));
        blogClassMapper.insertBatch(list);
    }

    public void topicSort(List<TopicClassDo> list) {
        Long classId = list.get(0).getClassId();
        topicClassMapper.delete(new LambdaQueryWrapper<TopicClassDo>().eq(TopicClassDo::getClassId,classId));
        topicClassMapper.insertBatch(list);
    }
}
