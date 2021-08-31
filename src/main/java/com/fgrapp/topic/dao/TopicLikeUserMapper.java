package com.fgrapp.topic.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.topic.domain.TopicLikeUserDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * TopicLikeUserMapper
 *
 * @author fan guang rui
 * @date 2021年08月29日 14:35
 */
@Mapper
@Component
public interface TopicLikeUserMapper extends FgrMapper<TopicLikeUserDo> {
}
