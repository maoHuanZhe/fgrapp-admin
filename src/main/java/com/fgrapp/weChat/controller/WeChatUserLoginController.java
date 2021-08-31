package com.fgrapp.weChat.controller;

import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.weChat.domain.WxCode2SessionPBO;
import com.fgrapp.weChat.service.WeChatUserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WeChatUserLoginController
 *
 * @author fan guang rui
 * @date 2021年08月23日 10:34
 */
@RestController
@RequestMapping("weChat")
@ResponseResultBody
@Api(tags = "微信控制器")
public class WeChatUserLoginController {
    private final WeChatUserLoginService service;

    public WeChatUserLoginController(WeChatUserLoginService service) {
        this.service = service;
    }

    @PostMapping("/miniLogin")
    @ApiOperation(value = "小程序授权登录", httpMethod = "POST")
    public SysUserDo login(@RequestBody WxCode2SessionPBO wxCode2SessionPBO) {
        return service.login(wxCode2SessionPBO);
    }
}
