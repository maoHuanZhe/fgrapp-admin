package com.fgrapp.weChat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WechatInfoConfig
 *
 * @author fan guang rui
 * @date 2021年08月23日 13:53
 */

@Data
@Component
@ConfigurationProperties(prefix = "fgr.wechat")
public class WechatInfoConfig {

    private String appId;

    private String appSecret;
}
