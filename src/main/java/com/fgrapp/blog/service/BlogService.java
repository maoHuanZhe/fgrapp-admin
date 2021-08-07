package com.fgrapp.blog.service;

import com.fgrapp.base.service.FgrService;
import com.fgrapp.blog.dao.BlogMapper;
import com.fgrapp.blog.domain.BlogDo;
import org.springframework.stereotype.Service;

/**
 * BlogService
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:19
 */
@Service
public class BlogService extends FgrService<BlogMapper, BlogDo> {
}
