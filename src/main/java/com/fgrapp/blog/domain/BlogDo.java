package com.fgrapp.blog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
public class BlogDo extends BaseDo {
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "封面图片")
    private String imgUrl;
    @ApiModelProperty(value = "内容")
    private String content;
}
