package com.fgrapp.blog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BlogUserDo
 *
 * @author fan guang rui
 * @date 2021年08月15日 18:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("func_blog_user")
public class BlogUserDo {

    private Long blogId;
    private Long userId;}
