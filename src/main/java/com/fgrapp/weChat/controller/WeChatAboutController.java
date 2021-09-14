package com.fgrapp.weChat.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.service.SysLoginService;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.weChat.service.WeChatAboutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * WeChatAboutController
 *
 * @author fan guang rui
 * @date 2021年09月09日 17:36
 */

@RestController
@RequestMapping("weChat/about")
@ResponseResultBody
@Api(tags = "微信小程序我的页面控制器")
public class WeChatAboutController {

    private final WeChatAboutService service;
    private final SysLoginService sysLoginService;

    public WeChatAboutController(WeChatAboutService service, SysLoginService sysLoginService) {
        this.service = service;
        this.sysLoginService = sysLoginService;
    }

    @SaCheckLogin
    @ApiOperation(value = "微信小程序获取用户信息")
    @GetMapping
    public Map<String,Object> getInfo(){
        //获取用户信息
        Map<String, Object> info = sysLoginService.getInfo();
        //获取操作数据
        Map<String, Object> map = service.getOperateNum(info.get("roles"));
        info.putAll(map);
        return info;
    }

    @GetMapping("search")
    @ApiOperation("小程序搜索列表")
    public IPage<List<Map<String,Object>>> search(@RequestParam Map<String,Object> map){
        return service.search(map);
    }
}
