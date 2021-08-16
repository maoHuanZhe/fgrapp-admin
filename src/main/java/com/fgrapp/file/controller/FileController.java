package com.fgrapp.file.controller;

import com.fgrapp.base.controller.FgrController;
import com.fgrapp.base.log.BusinessType;
import com.fgrapp.base.log.Log;
import com.fgrapp.base.result.ResponseResultBody;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.file.domain.FileResponse;
import com.fgrapp.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * FileController
 *
 * @author fan guang rui
 * @date 2021年07月31日 12:29
 */
@RestController
@RequestMapping("func/file")
@ResponseResultBody
@Api(tags = "文件控制器")
public class FileController extends FgrController {
    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping()
    @Log(title = "文件信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "文件上传",notes = "文件上传")
    public FileResponse upload(
            @ApiParam(name="file",value="文件",required=true)
            MultipartFile file) {
        return service.upload(file, FgrUtil.getUsername());
    }
    @GetMapping("list")
    @ApiOperation(value = "获取列表")
    public List<Map<String, Object>> list(){
        return service.listMaps();
    }
}
