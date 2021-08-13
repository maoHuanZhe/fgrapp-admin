package com.fgrapp.blog.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgrapp.base.dao.FgrMapper;
import com.fgrapp.blog.domain.ClassDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * ClassMapper
 *
 * @author fan guang rui
 * @date 2021年08月12日 12:42
 */
@Mapper
@Component
public interface ClassMapper extends FgrMapper<ClassDo> {
    IPage<List<Map<String, Object>>> getPage(Page<ClassDo> paramPage,@Param(Constants.WRAPPER) Map<String, Object> map);

    List<ClassDo> getListByBlogId(Long id);
}
