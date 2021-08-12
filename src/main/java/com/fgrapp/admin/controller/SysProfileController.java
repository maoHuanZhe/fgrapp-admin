package com.fgrapp.admin.controller;

import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.service.SysUserService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SysProfileController
 * 个人信息 业务处理
 *
 * @author fan guang rui
 * @date 2021年06月10日 15:48
 */
@Api(tags="个人信息 业务处理")
@RestController
@ResponseResultBody
@RequestMapping("/system/user/profile")
public class SysProfileController extends FgrController {
    private final SysUserService userService;

    public SysProfileController(SysUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "获取个人信息")
    @GetMapping
    public Map<String,Object> profile(){
        return userService.profile();
    }
    @ApiOperation(value = "修改用户")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public void updateProfile(@RequestBody SysUserDo user){
        userService.updateProfile(user);
    }

    @ApiOperation(value = "重置密码")
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public void updatePwd(String oldPassword, String newPassword) {
        userService.updatePwd(oldPassword,newPassword);
    }
}
