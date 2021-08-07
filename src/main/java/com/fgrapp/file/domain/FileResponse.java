package com.fgrapp.file.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * FileResponse
 *
 * @author fan guang rui
 * @date 2020年12月05日 8:32
 */
@Data
@TableName("func_file")
public class FileResponse {
    private String domain;
    private String md5;
    private String path;
    private String retmsg;
    private String scene;
    private String scenes;
    private String src;
    private String url;
    private long mtime;
    private long size;
    private int retcode;
    private String createAt;
    private Date createTime;
}
