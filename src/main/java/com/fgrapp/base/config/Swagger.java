package com.fgrapp.base.config;

import lombok.Data;

/**
 * Swagger
 *
 * @author fan guang rui
 * @date 2021年06月11日 19:08
 */
@Data
public class Swagger {
    /** 是否开启swagger */
    private boolean enabled;

    /** 设置请求的统一前缀 */
    private String pathMapping;
    /** 设置请求的统一前缀 */
    private String title;
    /** 简介 */
    private String description;
    /** 服务Url */
    private String termsOfServiceUrl;
    /** 作者  */
    private String name;
    private String url;
    private String email;

    /** 开源协议名称 */
    private String license;
    /** 开源协议地址 */
    private String licenseUrl;
    /** 版本 */
    private String version;
}
