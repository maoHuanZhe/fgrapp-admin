package com.fgrapp.blog.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * BlogDo
 *
 * @author fan guang rui
 * @date 2021年07月31日 9:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("func_blog")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="博客表",description="博客func_blog")
public class BlogDo extends BaseDo {
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "分类")
    private Integer type;
    @ApiModelProperty(value = "封面图片")
    private String imgUrl;
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "博客分类列表")
    @TableField(exist = false)
    private List<Long> classIds;
    @ApiModelProperty(value = "要新建的博客分类列表")
    @TableField(exist = false)
    private List<String> addClassNames;
}
