package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fgrapp.admin.domain.vo.server.Server;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ServerController
 *
 * @author fan guang rui
 * @date 2021年06月11日 16:32
 */
@Api(tags="服务器监控")
@RestController
@ResponseResultBody
@RequestMapping("/monitor/server")
public class ServerController {

    @ApiOperation(value = "获取服务器信息")
    @SaCheckPermission("monitor:server:list")
    @GetMapping()
    public Server getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return server;
    }
}
