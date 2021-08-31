package com.fgrapp.topic.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BlogClassDo
 *
 * @author fan guang rui
 * @date 2021年08月12日 12:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("func_topic_class")
public class TopicClassDo {
    private Long topicId;
    private Long classId;
    private Integer sortNum;
}
