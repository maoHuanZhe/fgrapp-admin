package com.fgrapp.blog.domain;

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
@TableName("func_comment")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="博客评论表",description="博客评论func_comment")
public class CommentDo extends BaseDo {
    @ApiModelProperty(value = "博客编号")
    private Long blogId;
    @ApiModelProperty(value = "上级评论编号")
    private Long parentId;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "审核通过(0:未通过,1:通过)")
    private String isAudit;
}
