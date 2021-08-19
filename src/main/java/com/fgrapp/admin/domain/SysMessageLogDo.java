package com.fgrapp.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SysMessageLogDo
 *
 * @author fan guang rui
 * @date 2021年08月19日 13:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_message_log")
@ApiModel(value="信息发送记录表",description="信息发送记录表sys_message_log")
public class SysMessageLogDo {
    @ApiModelProperty(value = "主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "内容")
    private String constent;
    @ApiModelProperty(value = "返回值")
    private String code;
    @ApiModelProperty(value = "返回内容")
    private String message;
    @ApiModelProperty(value = "请求编号")
    private String requestId;
    @ApiModelProperty(value = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime sentTime;
}
