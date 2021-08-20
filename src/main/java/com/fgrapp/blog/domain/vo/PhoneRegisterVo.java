package com.fgrapp.blog.domain.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * RegisterVo
 *
 * @author fan guang rui
 * @date 2021年08月17日 20:25
 */
@Data
public class PhoneRegisterVo {
    @NotEmpty(message = "手机号不能为空")
    @Length(min = 11,max = 11,message = "手机号长度应为十一位")
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    @Length(min = 6,max = 6,message = "验证码长度应为六位")
    private String code;
}
