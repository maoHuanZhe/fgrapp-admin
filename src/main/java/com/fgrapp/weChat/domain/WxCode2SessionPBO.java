package com.fgrapp.weChat.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WxCode2SessionPBO
 *
 * @author fan guang rui
 * @date 2021年08月23日 13:56
 */
@ApiModel("微信 code2session 入参")
@Data
public class WxCode2SessionPBO {

    @ApiModelProperty(value = "微信返回的code")
    private String code;

    @ApiModelProperty(value = "非敏感的用户信息")
    private String rawData;

    @ApiModelProperty(value = "签名信息")
    private String signature;

    @ApiModelProperty(value = "加密的数据")
    private String encrypteData;

    @ApiModelProperty(value = "加密密钥")
    private String iv;

}
