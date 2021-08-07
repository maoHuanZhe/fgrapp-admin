package com.fgrapp.file.service;

import com.fgrapp.base.service.FgrService;
import com.fgrapp.file.dao.FileMapper;
import com.fgrapp.file.domain.FileResponse;
import com.fgrapp.file.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileService
 *
 * @author fan guang rui
 * @date 2021年07月31日 12:27
 */
@Service
public class FileService extends FgrService<FileMapper, FileResponse> {
    @Autowired
    private FileMapper mapper;
    @Value("${fgr.goFastUrl}")
    private String baseUrl;

    public FileResponse upload(MultipartFile file,String userName) {
        FileResponse fileResponse = FileUtil.upload(file,baseUrl+"upload");
        if (fileResponse != null){
            // 获取当前的用户名称
            fileResponse.setCreateAt(userName);
            mapper.insert(fileResponse);
        }
        return fileResponse;
    }
}
