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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Date;

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
    @Value("${spring.mail.username}")
    private String sendEmail;
    @Autowired
    private SysMessageLogMapper logMapper;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
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
            insetMessageLog(body,phone,code);
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

    public boolean sendSimpleMail(String email,String code) {
        try {
            // 构建一个邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置邮件主题
            message.setSubject("【FGRAPP】注册登录验证码");
            // 设置邮件发送者，这个跟application.yml中设置的要一致
            message.setFrom(sendEmail);
            // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
            // message.setTo("10*****16@qq.com","12****32*qq.com");
            message.setTo(email);
            // 设置邮件抄送人，可以有多个抄送人
//        message.setCc("12****32*qq.com");
            // 设置隐秘抄送人，可以有多个
//        message.setBcc("7******9@qq.com");
            // 设置邮件发送日期
            message.setSentDate(new Date());
            // 设置邮件的正文
            message.setText(code);
            // 发送邮件
            javaMailSender.send(message);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendThymeleafMail(String email,String code) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            mimeMessage.setSubject("【FGRAPP】注册登录验证码");
            helper.setFrom(sendEmail);
            helper.setTo(email);
            helper.setSentDate(new Date());
            // 这里引入的是Template的Context
            Context context = new Context();
            // 设置模板中的变量
            context.setVariable("code", code);
            // 第一个参数为模板的名称
            String process = templateEngine.process("register.html", context);
            // 第二个参数true表示这是一个html文本
            helper.setText(process,true);
            javaMailSender.send(mimeMessage);
            return true;
        }  catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
