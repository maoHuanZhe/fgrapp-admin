package com.fgrapp.topic.domain;

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
@TableName("func_topic_operate_num")
public class TopicOperateNumDo {

    @TableId(value = "topicId",type = IdType.INPUT)
    private Long topicId;
    private Long readNum;
    private Long likeNum;
    private Long collectNum;
    @TableField(exist = false)
    private boolean canLike;
    @TableField(exist = false)
    private boolean canCollect;
}
