package com.fgrapp.blog.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.BlogDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * BlogMapper
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:19
 */
@Mapper
@Component
public interface BlogMapper extends FgrMapper<BlogDo> {
    IPage<List<Map<String, Object>>> getPage(Page<BlogDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);
}
