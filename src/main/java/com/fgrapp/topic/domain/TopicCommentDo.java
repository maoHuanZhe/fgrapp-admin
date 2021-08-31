package com.fgrapp.topic.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * CommentDo
 *
 * @author fan guang rui
 * @date 2021年08月13日 21:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("func_topic_comment")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="问题评论表",description="问题评论func_topic_comment")
public class TopicCommentDo extends BaseDo {
    @ApiModelProperty(value = "问题编号")
    private Long topicId;
    @ApiModelProperty(value = "上级评论编号")
    private Long parentId;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "审核通过(0:未通过,1:通过)")
    private String isAudit;
}
