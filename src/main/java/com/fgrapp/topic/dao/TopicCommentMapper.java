package com.fgrapp.topic.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.topic.domain.TopicCommentDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * TopicCommentMapper
 *
 * @author fan guang rui
 * @date 2021年08月29日 14:28
 */
@Mapper
@Component
public interface TopicCommentMapper extends FgrMapper<TopicCommentDo> {
    IPage<List<Map<String, Object>>> getPage(Page<TopicCommentDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);
}
