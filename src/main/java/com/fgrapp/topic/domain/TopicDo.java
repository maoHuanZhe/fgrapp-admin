package com.fgrapp.topic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * TopicDo
 *
 * @author fan guang rui
 * @date 2021年07月31日 9:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("func_topic")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="问题表",description="问题func_topic")
public class TopicDo extends BaseDo {
    @ApiModelProperty(value = "问题")
    private String problem;
    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "问题分类列表")
    @TableField(exist = false)
    private List<Long> classIds;
    @ApiModelProperty(value = "要新建的分类列表")
    @TableField(exist = false)
    private List<String> addClassNames;
}
