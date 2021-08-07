package com.fgrapp.file.utils;

import cn.hutool.json.JSONUtil;
import com.fgrapp.file.domain.FileResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FileUtil
 *
 * @author fan guang rui
 * @date 2020年12月05日 8:15
 */
@Slf4j
public class FileUtil {

    public static FileResponse upload(MultipartFile file, String url){
        try {
        OkHttpClient httpClient = new OkHttpClient();
        MultipartBody multipartBody = new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("multipart/form-data;charset=utf-8"),
                                file.getBytes()))
                .addFormDataPart("output", "json")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                String result;
                if (body != null) {
                    result = body.string();
                    FileResponse fileResponse = JSONUtil.toBean(result, FileResponse.class);
                    log.info("上传成功：{}",fileResponse);
                    return fileResponse;
                }
            } else {
                log.error("上传失败:{}",response.code());
            }
        } catch (IOException e) {
            log.error("上传失败:{}",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
