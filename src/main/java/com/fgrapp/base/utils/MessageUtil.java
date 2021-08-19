package com.fgrapp.base.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.fgrapp.admin.dao.SysMessageLogMapper;
import com.fgrapp.admin.domain.SysMessageLogDo;
import com.fgrapp.base.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 发送消息的工具类
 * 包含手机消息 邮件
 * MessageUtil
 *
 * @author fan guang rui
 * @date 2021年08月19日 11:18
 */
@Slf4j
@Component
public class MessageUtil {
    @Value("${fgr.ali.accessKeyId}")
    private String accessKeyId;
    @Value("${fgr.ali.accessKeySecret}")
    private String accessKeySecret;
    @Value("${fgr.ali.endpoint}")
    private String endpoint;
    @Value("${fgr.ali.message.signName}")
    private String signName;
    @Value("${fgr.ali.message.templateCode}")
    private String templateCode;
    @Autowired
    private SysMessageLogMapper logMapper;
    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception Exception
     */
    public Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new Client(config);
    }

    public boolean sendPhoneMessage(String phone,String code){
        Client client;
        try {
            client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\""+code+"\"}");
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.getBody();
            String bodyCode = body.getCode();
            //记录短信发送日志

            if (Constants.OK.equals(bodyCode)){
                //调用成功
                return true;
            } else {
                //调用失败
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void insetMessageLog(SendSmsResponseBody body,String phone,String code){
        SysMessageLogDo messageLogDo = SysMessageLogDo.builder().phone(phone).constent(code).sentTime(LocalDateTime.now())
                .code(body.getCode()).message(body.getMessage()).requestId(body.getRequestId()).build();
        logMapper.insert(messageLogDo);
    }
    /**
     * {
     *   "RequestId": "9D20D2D3-9C7F-5B10-982A-90E00201687C",
     *   "Message": "账户余额不足",
     *   "Code": "isv.AMOUNT_NOT_ENOUGH"
     * }
     * {
     *   "RequestId": "BC3436D2-6FD3-50F8-B5F3-D22DAF5F69DD",
     *   "Message": "OK",
     *   "BizId": "324523429349303435^0",
     *   "Code": "OK"
     * }
     */
}
