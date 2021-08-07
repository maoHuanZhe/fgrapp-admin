package com.fgrapp.blog.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.BlogDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * BlogMapper
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:19
 */
@Mapper
@Component
public interface BlogMapper extends FgrMapper<BlogDo> {
}
