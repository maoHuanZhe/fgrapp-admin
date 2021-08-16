package com.fgrapp.blog.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.CommentDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * CommentMapper
 *
 * @author fan guang rui
 * @date 2021年08月13日 21:12
 */
@Mapper
@Component
public interface CommentMapper extends FgrMapper<CommentDo> {
    IPage<List<Map<String, Object>>> getPage(Page<CommentDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);
}
