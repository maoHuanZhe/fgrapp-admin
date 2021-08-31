package com.fgrapp.topic.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.topic.domain.TopicDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * TopicMapper
 *
 * @author fan guang rui
 * @date 2021年08月26日 18:46
 */
@Mapper
@Component
public interface TopicMapper extends FgrMapper<TopicDo> {
    IPage<List<Map<String, Object>>> getPage(Page<TopicDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);

    void dels(Long id);

    IPage<List<Map<String, Object>>> getTopicPage(Page<TopicDo> paramPage, @Param(Constants.WRAPPER) Map<String, Object> map);
}
