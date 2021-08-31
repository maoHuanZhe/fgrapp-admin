package com.fgrapp.weChat.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * WeChatUtil
 *
 * @author fan guang rui
 * @date 2021年08月23日 10:30
 */
public class WeChatUtil {
    public final static String JS_CODE2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 获取SessionKey
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public static JSONObject getSessionKeyOrOpenId(String appid, String secret, String code) {
        HashMap<String, Object> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", appid);
        //小程序secret
        requestUrlParam.put("secret", secret);
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(JS_CODE2_SESSION_URL, requestUrlParam);
        return JSONUtil.parseObj(result);
    }
}
