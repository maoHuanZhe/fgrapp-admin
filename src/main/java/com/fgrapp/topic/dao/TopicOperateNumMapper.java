package com.fgrapp.topic.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.topic.domain.TopicOperateNumDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * TopicOperateNumMapper
 *
 * @author fan guang rui
 * @date 2021年08月29日 9:13
 */
@Mapper
@Component
public interface TopicOperateNumMapper extends FgrMapper<TopicOperateNumDo> {
    void updateReadNum(Long topicId);

    void updateLickNum(@Param("topicId") Long topicId, @Param("num") int num);

    void updateCollectNum(@Param("topicId") Long topicId, @Param("num") int num);
}
