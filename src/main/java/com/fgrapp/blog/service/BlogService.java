package com.fgrapp.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.BlogMapper;
import com.fgrapp.blog.domain.BlogDo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * BlogService
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:19
 */
@Service
public class BlogService extends FgrService<BlogMapper, BlogDo> {
    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,BlogDo.class),map);
    }
}
