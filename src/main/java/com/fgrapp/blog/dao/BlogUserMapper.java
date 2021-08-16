package com.fgrapp.blog.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.BlogUserDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * BlogUserMapper
 *
 * @author fan guang rui
 * @date 2021年08月15日 19:35
 */
@Mapper
@Component
public interface BlogUserMapper extends FgrMapper<BlogUserDo> {
}
