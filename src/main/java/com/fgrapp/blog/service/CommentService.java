package com.fgrapp.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.blog.dao.CommentMapper;
import com.fgrapp.blog.domain.CommentDo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * CommentService
 *
 * @author fan guang rui
 * @date 2021年08月13日 21:13
 */
@Service
public class CommentService extends FgrService<CommentMapper, CommentDo> {
    public void dels(List<Long> ids) {
        //判断评论是否已经审批通过 审批通过的评论不能删除
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<CommentDo>()
                .eq(CommentDo::getIsAudit, 1)
                .in(CommentDo::getId, ids));
        if (count > 0){
            throw new ResultException("存在审核通过的评论，不能删除");
        }
        baseMapper.deleteBatchIds(ids);
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,CommentDo.class),map);
    }
}
