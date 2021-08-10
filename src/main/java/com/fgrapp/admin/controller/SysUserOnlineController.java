package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpUtil;
import com.fgrapp.admin.domain.SysLogininforDo;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * SysUserOnlineController
 * 在线用户监控
 *
 * @author fan guang rui
 * @date 2021年06月11日 20:55
 */
@Api(tags="在线用户监控")
@RestController
@ResponseResultBody
@RequestMapping("/monitor/online")
public class SysUserOnlineController {

    @ApiOperation(value = "获取在线用户列表")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    public List<SysLogininforDo> page(String ipaddr, String userName){
        // 查询所有账号Session会话
        List<String> list = StpUtil.searchSessionId("", 0, 100);
        List<SysLogininforDo> userOnlineList = new ArrayList<>();
        list.forEach(item ->{
            // 获取SessionId为xxxx-xxxx的Session, 在Session尚未创建时, 返回null
            SaSession saSession = StpUtil.getSessionBySessionId(item);
            List<TokenSign> tokenSignList = saSession.getTokenSignList();
            tokenSignList.forEach(tokenSign -> {
                String tokenSignValue = tokenSign.getValue();
                // 获取指定token的专属Session
                SaSession tokenSessionByToken = StpUtil.getTokenSessionByToken(tokenSignValue);
                SysLogininforDo logininforDo = tokenSessionByToken.getModel(UserConstants.LOGIN_INFO, SysLogininforDo.class);
                logininforDo.setTokenId(tokenSignValue);
                userOnlineList.add(logininforDo);
            });
        });
        return userOnlineList;
    }
    @ApiOperation(value = "强退用户")
    @SaCheckPermission("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public Result<Void> forceLogout(@PathVariable String tokenId){
        StpUtil.logoutByTokenValue(tokenId);
        return Result.success();
    }
    @ApiOperation(value = "用户封禁")
    @Log(title = "在线用户", businessType = BusinessType.DISABLE)
    @DeleteMapping("/{tokenId}/{time}")
    public Result<Void> forceDisable(@PathVariable String tokenId,@PathVariable Long time){
        SaSession tokenSessionByToken = StpUtil.getTokenSessionByToken(tokenId);
        SysUserDo userDo = tokenSessionByToken.getModel(UserConstants.USER_KEY, SysUserDo.class);
        Long id = userDo.getId();
        StpUtil.logoutByLoginId(id);
        StpUtil.disable(id, time);
        return Result.success();
    }

}
