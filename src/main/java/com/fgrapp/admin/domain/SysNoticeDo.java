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
 * SysNoticeDo
 *
 * @author fan guang rui
 * @date 2021年08月11日 12:33
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_notice")
@ApiModel(value="通知公告表",description="通知公告表sys_notice")
public class SysNoticeDo extends BaseDo {
    @ApiModelProperty(value = "公告标题")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    private String noticeTitle;

    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    @ApiModelProperty(value = "公告状态（0正常 1关闭）")
    private String status;
}
