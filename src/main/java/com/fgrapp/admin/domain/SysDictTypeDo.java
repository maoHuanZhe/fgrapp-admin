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
 * SysDictTypeDo
 *
 * @author fan guang rui
 * @date 2021年08月10日 12:31
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict_type")
@ApiModel(value="字典类型表",description="字典类型表sys_dict_type")
public class SysDictTypeDo extends BaseDo{

    @ApiModelProperty(value = "字典名称")
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典类型名称长度不能超过100个字符")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型类型长度不能超过100个字符")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;
}
