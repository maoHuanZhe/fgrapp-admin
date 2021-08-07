package com.fgrapp.admin.domain.vo;

import lombok.Data;

/**
 * SysUserOnline
 * 当前在线会话
 *
 * @author fan guang rui
 * @date 2021年06月11日 21:01
 */
@Data
public class SysUserOnline {
    /** 会话编号 */
    private String tokenId;

    /** 部门名称 */
    private String deptName;

    /** 用户名称 */
    private String userName;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地址 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录时间 */
    private Long loginTime;
}
