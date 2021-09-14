package com.fgrapp.blog.dao;

import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.BlogOperateNumDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * BlogOperateNumMapper
 *
 * @author fan guang rui
 * @date 2021年08月15日 18:03
 */
@Mapper
@Component
public interface BlogOperateNumMapper extends FgrMapper<BlogOperateNumDo> {
    void updateReadNum(Long blogId);
    void updateLickNum(@Param("blogId") Long blogId, @Param("num") int num);

    void updateCollectNum(@Param("blogId") Long blogId, @Param("num") int num);
}
