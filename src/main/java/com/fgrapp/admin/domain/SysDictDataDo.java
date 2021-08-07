package com.fgrapp.admin.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.domain.BaseDo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典数据表 sys_dict_data
 *
 * @author fan guang rui
 * @date 2021年06月09日 18:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict_data")
@ApiModel(value="字典数据表",description="字典数据表sys_dict_data")
public class SysDictDataDo extends BaseDo {


    @ApiModelProperty(value = "字典排序")
    private Long dictSort;

    @ApiModelProperty(value = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    @ApiModelProperty(value = "字典键值")
    @NotBlank(message = "字典键值不能为空")
    @Size(max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    @ApiModelProperty(value = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    @ApiModelProperty(value = "样式属性（其他样式扩展）")
    @Size(max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    @ApiModelProperty(value = "表格字典样式")
    private String listClass;

    @ApiModelProperty(value = "是否默认（Y是 N否）")
    private String isDefault;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    public boolean getDefault()
    {
        return UserConstants.YES.equals(this.isDefault);
    }


}
