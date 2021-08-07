package com.fgrapp.admin.domain;

import lombok.Data;

/**
 * LoginBody
 *
 * @author fan guang rui
 * @date 2021年06月09日 18:24
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid = "";
    /**
     * 记住我
     */
    private Boolean rememberMe;
}
