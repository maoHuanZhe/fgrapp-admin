package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.service.SysLogininforService;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SysLogininforController
 *
 * @author fan guang rui
 * @date 2021年08月05日 13:08
 */
@RestController
@RequestMapping("/monitor/logininfor")
@ResponseResultBody
@Api(tags = "登陆日志控制器")
public class SysLogininforController {
    private final SysLogininforService service;

    public SysLogininforController(SysLogininforService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("monitor:logininfor:list")
    @GetMapping("/page")
    public IPage<List<Map<String, Object>>> list(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public Boolean remove(@PathVariable List<Long> infoIds){
        return service.removeByIds(infoIds);
    }

    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Boolean clean(){
        return service.remove(new QueryWrapper<>());
    }
}
