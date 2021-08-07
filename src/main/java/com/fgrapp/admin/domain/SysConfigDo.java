package com.fgrapp.admin.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * SysConfigDo
 *
 * @author fan guang rui
 * @date 2021年08月06日 21:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_config")
@ApiModel(value="参数配置表",description="参数配置表sys_config")
public class SysConfigDo extends BaseDo {

    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    @ApiModelProperty(value = "参数名称")
    private String configName;

    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    @ApiModelProperty(value = "参数键值")
    private String configValue;

    @ApiModelProperty(value = "系统内置（Y是 N否）")
    private String configType;
}
