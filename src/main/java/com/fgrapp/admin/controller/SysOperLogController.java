package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.service.SysOperLogService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SysOperLogController
 *
 * @author fan guang rui
 * @date 2021年07月31日 12:08
 */
@RestController
@RequestMapping("/monitor/operlog")
@ResponseResultBody
@Api(tags = "操作日志控制器")
public class SysOperLogController extends FgrController {
    private final SysOperLogService service;

    public SysOperLogController(SysOperLogService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("monitor:operlog:list")
    @GetMapping("/page")
    public IPage<List<Map<String, Object>>> list(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }
    @SaCheckPermission("monitor:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public Boolean remove(@PathVariable List<Long> infoIds){
        return service.removeByIds(infoIds);
    }

    @SaCheckPermission("monitor:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Boolean clean(){
        return service.remove(new QueryWrapper<>());
    }
}
