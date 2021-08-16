package com.fgrapp.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BlogOperateNumDo
 *
 * @author fan guang rui
 * @date 2021年08月15日 18:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("func_blog_operate_num")
public class BlogOperateNumDo {

    @TableId(value = "blogId",type = IdType.INPUT)
    private Long blogId;
    private Long readNum;
    private Long likeNum;
    @TableField(exist = false)
    private boolean canLike;
}
