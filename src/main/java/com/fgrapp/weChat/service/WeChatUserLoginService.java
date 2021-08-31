package com.fgrapp.weChat.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgrapp.admin.dao.SysUserMapper;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.service.SysLoginService;
import com.fgrapp.weChat.config.WechatInfoConfig;
import com.fgrapp.weChat.domain.WxCode2SessionPBO;
import com.fgrapp.weChat.util.WeChatUtil;
import org.springframework.stereotype.Service;

/**
 * WeChatUserLoginService
 *
 * @author fan guang rui
 * @date 2021年08月23日 10:38
 */
@Service
public class WeChatUserLoginService {
    private final SysUserMapper userMapper;
    private final WechatInfoConfig wechatInfoConfig;
    private final SysLoginService loginService;
    public WeChatUserLoginService(SysUserMapper userMapper, WechatInfoConfig wechatInfoConfig, SysLoginService loginService) {
        this.userMapper = userMapper;
        this.wechatInfoConfig = wechatInfoConfig;
        this.loginService = loginService;
    }

    public SysUserDo login(WxCode2SessionPBO wxCode2SessionPBO) {
        //开发者服务器 登录凭证校验接口 appId + appSecret + 接收小程序发送的code
        JSONObject sessionKeyOpenId = WeChatUtil.getSessionKeyOrOpenId(wechatInfoConfig.getAppId(),
                wechatInfoConfig.getAppSecret(), wxCode2SessionPBO.getCode());
        //接收微信接口服务 获取返回的参数
        String openid = sessionKeyOpenId.get("openid", String.class);
//        String sessionKey = sessionKeyOpenId.get("session_key", String.class);
        //用户非敏感信息：rawData
        //签名：signature
        JSONObject rawDataJson = JSONUtil.parseObj(wxCode2SessionPBO.getRawData());
        //判断是否已注册
        SysUserDo userDo = userMapper.selectOne(new LambdaQueryWrapper<SysUserDo>()
                .eq(SysUserDo::getOpenId, openid));
        if (ObjectUtil.isNull(userDo)){
            //用户不存在 注册用户
            //新增用户信息
            userDo = new SysUserDo();
            userDo.setUserName(rawDataJson.getStr("nickName"));
            userDo.setNickName("mini_"+rawDataJson.getStr("nickName"));
            userDo.setAvatar(rawDataJson.getStr("avatarUrl"));
            userDo.setStatus("0");
            userDo.setSex(rawDataJson.getStr("gender"));
            userDo.setOpenId(openid);
            userMapper.insert(userDo);
        }
        //用户已存在 直接登录
        String token = loginService.userLogin(userDo);
        userDo.setPassword(token);
        return userDo;
    }
}
