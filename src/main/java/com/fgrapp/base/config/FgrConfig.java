package com.fgrapp.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * FgrConfig
 *读取项目相关配置
 * @author fan guang rui
 * @date 2021年06月09日 19:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "fgr")
public class FgrConfig {
    private Swagger swagger;
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 上传路径 */
    private String profile;

    /** 获取地址开关 */
    private boolean addressEnabled;
    /** 获取地址开关 */
    private String goFastUrl;

    public boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        this.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    public String getProfile() {
        return profile;
    }

    /**
     * 获取下载路径
     */
    public String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
