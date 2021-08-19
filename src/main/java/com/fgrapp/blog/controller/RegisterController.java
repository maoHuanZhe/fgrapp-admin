package com.fgrapp.blog.controller;

import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.blog.domain.vo.RegisterVo;
import com.fgrapp.blog.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * RegisterController
 *
 * @author fan guang rui
 * @date 2021年08月18日 11:02
 */
@RestController
@RequestMapping("/register")
@ResponseResultBody
@Api(tags = "注册登录控制器")
public class RegisterController extends FgrController {
    @Autowired
    private RegisterService service;
    @GetMapping("/{phone}")
    @ApiOperation("发送验证码")
    public void sendMessage(@PathVariable String phone){
        service.sendMessage(phone);
    }
    @PostMapping("/phone")
    @ApiOperation("手机号注册")
    public String register(@Validated @RequestBody RegisterVo info){
        return service.register(info);
    }
}
