package com.fgrapp.admin.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.fgrapp.admin.domain.LoginBody;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.domain.vo.RouterVo;
import com.fgrapp.admin.service.SysLoginService;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SysLoginController
 * 登录验证
 *
 * @author fan guang rui
 * @date 2021年06月09日 18:21
 */
@Api(tags="登录验证")
@RestController
@ResponseResultBody
public class SysLoginController {

    @Autowired
    private SysLoginService service;

    /**
     * 生成验证码
     */
    @ApiOperation(value = "生成验证码",notes = "生成验证码")
    @GetMapping("/captchaImage")
    public Map<String,String> getCode() throws IOException {
        return service.getCode();
    }

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public String login(@RequestBody LoginBody loginBody){
        return service.login(loginBody);
    }
    @ApiOperation(value = "获取用户信息")
    @GetMapping("getInfo")
    @SaCheckLogin
    public Map<String,Object> getInfo() {
        return service.getInfo();
    }
    @ApiOperation(value = "获取路由信息")
    @GetMapping("getRouters")
    @SaCheckLogin
    public List<RouterVo> getRouters(){
        return service.getRouters();
    }
    @ApiOperation(value = "登出")
    @PostMapping("logout")
    public Void logout(){
        return service.logout();
    }
}

