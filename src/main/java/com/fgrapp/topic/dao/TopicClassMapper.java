package com.fgrapp.topic.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.topic.domain.TopicClassDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * TopicClassMapper
 *
 * @author fan guang rui
 * @date 2021年08月29日 9:13
 */
@Mapper
@Component
public interface TopicClassMapper extends FgrMapper<TopicClassDo> {
}
