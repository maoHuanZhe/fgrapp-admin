package com.fgrapp.blog.domain.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * RegisterVo
 *
 * @author fan guang rui
 * @date 2021年08月17日 20:25
 */
@Data
public class EmailRegisterVo {
    @NotEmpty(message = "邮箱不能为空")
    @Email
    private String email;
    @NotEmpty(message = "验证码不能为空")
    @Length(min = 6,max = 6,message = "验证码长度应为六位")
    private String code;
}
