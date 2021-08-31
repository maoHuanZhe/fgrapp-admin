package com.fgrapp.topic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.PageUtil;
import com.fgrapp.topic.dao.TopicCommentMapper;
import com.fgrapp.topic.domain.TopicCommentDo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TopicCommentService
 *
 * @author fan guang rui
 * @date 2021年08月30日 17:58
 */
@Service
public class TopicCommentService extends FgrService<TopicCommentMapper, TopicCommentDo> {
    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,TopicCommentDo.class),map);
    }

    public void dels(List<Long> ids) {
        //判断评论是否已经审批通过 审批通过的评论不能删除
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<TopicCommentDo>()
                .eq(TopicCommentDo::getIsAudit, 1)
                .in(TopicCommentDo::getId, ids));
        if (count > 0){
            throw new ResultException("存在审核通过的评论，不能删除");
        }
        baseMapper.deleteBatchIds(ids);
    }
}
