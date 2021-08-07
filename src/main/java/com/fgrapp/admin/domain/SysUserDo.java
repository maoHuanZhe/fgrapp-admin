package com.fgrapp.admin.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import com.fgrapp.base.utils.FgrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * SysUser
 * 用户对象 sys_user
 *
 * @author fan guang rui
 * @date 2021年06月09日 18:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
@ApiModel(value="用户对象",description="用户对象 sys_user")
public class SysUserDo extends BaseDo {

    private static final long serialVersionUID = 103773122639812862L;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phonenumber;

    @ApiModelProperty(value = "用户性别")
    private String sex;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "盐加密")
    @TableField(exist = false)
    private String salt;

    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginDate;

    @ApiModelProperty(value = "角色对象")
    @TableField(exist = false)
    private List<SysRoleDo> roles;

    @ApiModelProperty(value = "角色组")
    @TableField(exist = false)
    private Long[] roleIds;

    public boolean isAdmin(){
        return FgrUtil.isAdmin(this.getId());
    }
}
