package com.fgrapp.admin.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author fan guang rui
 * @date 2021年06月09日 18:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_menu")
@ApiModel(value="菜单权限表",description="菜单权限表sys_menu")
public class SysMenuDo extends BaseDo {

    private static final long serialVersionUID = -5195718351360988339L;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    @ApiModelProperty(value = "父菜单名称")
    @TableField(exist = false)
    private String parentName;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序")
    @NotBlank(message = "显示顺序不能为空")
    private String orderNum;

    @ApiModelProperty(value = "路由地址")
    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;

    @ApiModelProperty(value = "组件路径")
    @Size(max = 200, message = "组件路径不能超过255个字符")
    private String component;

    @ApiModelProperty(value = "是否为外链（0是 1否）")
    private String isFrame;

    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    private String isCache;

    @ApiModelProperty(value = "类型（M目录 C菜单 F按钮）")
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    @ApiModelProperty(value = "显示状态（0显示 1隐藏）")
    private String visible;
    
    @ApiModelProperty(value = "菜单状态（0显示 1隐藏）")
    private String status;

    @ApiModelProperty(value = "权限字符串")
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<SysMenuDo> children = new ArrayList<>();
}
