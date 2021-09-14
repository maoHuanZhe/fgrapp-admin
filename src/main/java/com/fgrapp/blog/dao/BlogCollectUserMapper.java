package com.fgrapp.blog.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.BlogCollectUserDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * BlogCollectUserMapper
 *
 * @author fan guang rui
 * @date 2021年09月11日 16:08
 */
@Mapper
@Component
public interface BlogCollectUserMapper extends FgrMapper<BlogCollectUserDo> {
}
