package com.fgrapp.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.domain.SysNoticeDo;
import com.fgrapp.admin.service.SysNoticeService;
import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SysNoticeController
 *
 * @author fan guang rui
 * @date 2021年08月11日 12:39
 */
@RestController
@RequestMapping("/system/notice")
@ResponseResultBody
@Api(tags = "公告控制器")
public class SysNoticeController extends FgrController {
    private final SysNoticeService service;

    public SysNoticeController(SysNoticeService service) {
        this.service = service;
    }

    @ApiOperation(value = "查询分页数据")
    @SaCheckPermission("system:notice:list")
    @GetMapping("/page")
    public IPage<List<Map<String,Object>>> page(@RequestParam Map<String,Object> map){
        return service.getPage(map);
    }

    @ApiOperation(value = "根据通知公告编号获取详细信息")
    @SaCheckPermission("system:notice:query")
    @Cacheable(value = "notice",unless = "#result == null")
    @GetMapping("/{id}")
    public SysNoticeDo getInfo(@PathVariable Long id){
        return service.getById(id);
    }
    @ApiOperation(value = "新增通知公告")
    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public boolean save(@Validated @RequestBody SysNoticeDo info){
        return service.save(info);
    }
    @ApiOperation(value = "修改通知公告")
    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    @CachePut(value = "notice",key = "#info.id")
    public SysNoticeDo edit(@Validated @RequestBody SysNoticeDo info){
        service.updateById(info);
        return info;
    }
    @ApiOperation(value = "删除通知公告")
    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @CacheEvict(value = "notice",allEntries = true)
    public void dels(@PathVariable List<Long> ids){
        service.removeByIds(ids);
    }
}
