package com.fgrapp.blog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * ClassDo
 *
 * @author fan guang rui
 * @date 2021年08月12日 12:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("func_class")
@NoArgsConstructor
@AllArgsConstructor
public class ClassDo extends BaseDo {
    @ApiModelProperty(value = "分类名称")
    private String name;
}
