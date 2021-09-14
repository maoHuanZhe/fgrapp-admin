package com.fgrapp.topic.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.ClassMapper;
import com.fgrapp.blog.domain.ClassDo;
import com.fgrapp.topic.dao.*;
import com.fgrapp.topic.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TopicService
 *
 * @author fan guang rui
 * @date 2021年08月29日 9:23
 */
@Slf4j
@Service
public class TopicService extends FgrService<TopicMapper, TopicDo> {

    private final TopicLikeUserMapper likeUserMapper;
    private final TopicCollectUserMapper collectUserMapper;
    private final TopicOperateNumMapper operateNumMapper;
    private final ClassMapper classMapper;
    private final TopicClassMapper topicClassMapper;
    private final TopicCommentMapper commentMapper;

    public TopicService(TopicOperateNumMapper operateNumMapper, ClassMapper classMapper, TopicClassMapper topicClassMapper, TopicCommentMapper commentMapper, TopicLikeUserMapper likeUserMapper, TopicCollectUserMapper collectUserMapper) {
        this.operateNumMapper = operateNumMapper;
        this.classMapper = classMapper;
        this.topicClassMapper = topicClassMapper;
        this.commentMapper = commentMapper;
        this.likeUserMapper = likeUserMapper;
        this.collectUserMapper = collectUserMapper;
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,TopicDo.class),map);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(TopicDo info) {
        //新增问题
        baseMapper.insert(info);
        Long topicId = info.getId();
        //新增问题操作数据
        operateNumMapper.insert(TopicOperateNumDo.builder()
                .topicId(topicId).likeNum(0L).readNum(0L).collectNum(0L).build());
        List<TopicClassDo> blogClassDos = new ArrayList<>();
        //判断是否有需要添加的分类
        List<String> addClassNames = info.getAddClassNames();
        if (ObjectUtil.isNotNull(addClassNames)){
            log.info("存在需要新增的分类");
            addClassNames.forEach(className -> {
                log.info("新增分类:{}",className);
                ClassDo classDo = ClassDo.builder().name(className).build();
                classMapper.insert(classDo);
                blogClassDos.add(new TopicClassDo(topicId,classDo.getId(),0));
            });
        }
        //添加问题与分类关联
        List<Long> classIds = info.getClassIds();
        if (ObjectUtil.isNotNull(classIds)){
            log.info("添加已存在的分类与问题关联");
            classIds.forEach(classId -> blogClassDos.add(new TopicClassDo(topicId,classId,0)));
        }
        if (blogClassDos.size() > 0){
            int allBatch = topicClassMapper.insertBatch(blogClassDos);
            log.info("新增{}条问题分类关联",allBatch);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public TopicDo updeate(TopicDo info) {
        Long topicId = info.getId();
        int delete = topicClassMapper.delete(new LambdaUpdateWrapper<TopicClassDo>()
                .eq(TopicClassDo::getTopicId, topicId));
        log.info("删除原有问题分类信息,删除的关联数量：{}",delete);
        List<TopicClassDo> blogClassDos = new ArrayList<>();
        //判断是否有需要添加的分类
        List<String> addClassNames = info.getAddClassNames();
        if (ObjectUtil.isNotNull(addClassNames)){
            log.info("存在需要新增的分类");
            addClassNames.forEach(className -> {
                log.info("新增分类:{}",className);
                ClassDo classDo = ClassDo.builder().name(className).build();
                classMapper.insert(classDo);
                blogClassDos.add(new TopicClassDo(topicId,classDo.getId(),0));
            });
        }
        //添加问题与分类关联
        List<Long> classIds = info.getClassIds();
        if (ObjectUtil.isNotNull(classIds)){
            log.info("添加已存在的分类与问题关联");
            classIds.forEach(classId -> blogClassDos.add(new TopicClassDo(topicId,classId,0)));
        }
        if (blogClassDos.size() > 0){
            int allBatch = topicClassMapper.insertBatch(blogClassDos);
            log.info("新增{}条问题分类关联",allBatch);
        }
        //获取问题摘要
        List<String> classNames = getClassNames(topicId);
        info.setAddClassNames(classNames);
        return info;
    }
    private List<String> getClassNames(Long id) {
        List<ClassDo> classDoList = classMapper.getListByTopicId(id);
        List<String> classNames = new ArrayList<>();
        classDoList.forEach(item ->{
            classNames.add(item.getName());
        });
        return classNames;
    }

    public void dels(Long id) {
        baseMapper.dels(id);
    }

    public Map<String,Object> getTopicPage(Map<String, Object> map) {
        Map<String,Object> returnMap = new HashMap<>(2);
        IPage<List<Map<String, Object>>> page = baseMapper.getTopicPage(PageUtil.getParamPage(map, TopicDo.class), map);
        //判断需不需要获取分类数据
        return getStringObjectMap(map, returnMap, page, classMapper);
    }


    public TopicDo getInfo(Long id) {
        //获取基本信息
        TopicDo topicDo = baseMapper.selectById(id);
        //获取分类信息
        List<ClassDo> classDoList = classMapper.getListByTopicId(id);
        List<String> classNames = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();
        classDoList.forEach(item ->{
            classNames.add(item.getName());
            classIds.add(item.getId());
        });
        topicDo.setAddClassNames(classNames);
        topicDo.setClassIds(classIds);
        return topicDo;
    }

    public Map<String, Object> getDetailInfo(Long id) {
        Map<String,Object> map = new HashMap<>(3);
        TopicDo topicDo = getInfo(id);
        TopicOperateNumDo operateNum = getOperateNum(id);
        List<TopicCommentDo> commentDos = commentMapper.selectList(new LambdaQueryWrapper<TopicCommentDo>()
                .eq(TopicCommentDo::getTopicId, id)
                .eq(TopicCommentDo::getIsAudit, 1)
                .orderByDesc(TopicCommentDo::getCreateTime));
        Long classId = topicDo.getClassIds().get(0);
        List<TopicClassDo> topicClassDos = topicClassMapper.selectList(new LambdaQueryWrapper<TopicClassDo>()
                .eq(TopicClassDo::getClassId, classId).orderByAsc(TopicClassDo::getSortNum));
        List<Long> ids = new ArrayList<>();
        topicClassDos.forEach(item -> ids.add(item.getTopicId()));
        map.put("topic",topicDo);
        map.put("operateNum",operateNum);
        map.put("commentDos",commentDos);
        map.put("ids",ids);
        return map;
    }
    public TopicOperateNumDo getOperateNum(Long id) {
        //问题阅读数加一
        operateNumMapper.updateReadNum(id);
        TopicOperateNumDo operateNumDo = operateNumMapper.selectById(id);
        //判断当前用户能否点赞
        if (!StpUtil.isLogin()){
            //未登陆
            operateNumDo.setCanLike(true);
            operateNumDo.setCanCollect(true);
        } else {
            Long userId = FgrUtil.getUserId();
            Integer count = likeUserMapper.selectCount(new LambdaQueryWrapper<TopicLikeUserDo>()
                    .eq(TopicLikeUserDo::getTopicId, id)
                    .eq(TopicLikeUserDo::getUserId, userId));
            operateNumDo.setCanLike(count == 0);
            Integer selectCount = collectUserMapper.selectCount(new LambdaQueryWrapper<TopicCollectUserDo>()
                    .eq(TopicCollectUserDo::getTopicId, id)
                    .eq(TopicCollectUserDo::getUserId, userId));
            operateNumDo.setCanCollect(selectCount == 0);
        }
        return operateNumDo;
    }

    @Transactional(rollbackFor = Exception.class)
    public TopicOperateNumDo updateLickNum(Long id, int num) {
        //点赞数量改变
        Long userId = FgrUtil.getUserId();
        if (num > 0){
            //点赞操作
            Integer count = likeUserMapper.selectCount(new LambdaQueryWrapper<TopicLikeUserDo>()
                    .eq(TopicLikeUserDo::getTopicId, id)
                    .eq(TopicLikeUserDo::getUserId, userId));
            if (count == 0){
                //添加问题与用户点赞关联
                likeUserMapper.insert(new TopicLikeUserDo(id,userId));
                operateNumMapper.updateLickNum(id,num);
            }
        } else {
            //取消点赞操作
            //删除问题与用户点赞关联
            likeUserMapper.delete(new LambdaUpdateWrapper<TopicLikeUserDo>()
                    .eq(TopicLikeUserDo::getUserId,userId)
                    .eq(TopicLikeUserDo::getTopicId,id));
            operateNumMapper.updateLickNum(id,num);
        }
        return getOperateNum(id);
    }

    public TopicOperateNumDo updateCollectNum(Long id, int num) {
        //收藏数量改变
        Long userId = FgrUtil.getUserId();
        if (num > 0){
            //收藏操作
            Integer selectCount = collectUserMapper.selectCount(new LambdaQueryWrapper<TopicCollectUserDo>()
                    .eq(TopicCollectUserDo::getTopicId, id)
                    .eq(TopicCollectUserDo::getUserId, userId));
            if (selectCount == 0) {
                //添加问题与用户收藏关联
                collectUserMapper.insert(new TopicCollectUserDo(id, userId));
                operateNumMapper.updateCollectNum(id, num);
            }
        } else {
            //取消收藏操作
            //删除问题·与用户收藏关联
            collectUserMapper.delete(new LambdaUpdateWrapper<TopicCollectUserDo>()
                    .eq(TopicCollectUserDo::getUserId,userId)
                    .eq(TopicCollectUserDo::getTopicId,id));
                operateNumMapper.updateCollectNum(id,num);
        }
        return getOperateNum(id);
    }
}
