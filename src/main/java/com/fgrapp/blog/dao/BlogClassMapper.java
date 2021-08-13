package com.fgrapp.blog.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.BlogClassDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * BlogClassMapper
 *
 * @author fan guang rui
 * @date 2021年08月12日 12:49
 */
@Mapper
@Component
public interface BlogClassMapper extends FgrMapper<BlogClassDo> {
}
