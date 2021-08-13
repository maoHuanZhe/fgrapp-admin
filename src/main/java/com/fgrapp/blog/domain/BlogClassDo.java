package com.fgrapp.blog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
@TableName("func_blog_class")
public class BlogClassDo {
    private Long blogId;
    private Long classId;
}
