package com.fgrapp.blog.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgrapp.admin.dao.SysUserMapper;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.service.SysLoginService;
import com.fgrapp.base.config.redis.RedisCache;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.MessageUtil;
import com.fgrapp.blog.domain.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * RegisterService
 *
 * @author fan guang rui
 * @date 2021年08月18日 11:04
 */
@Slf4j
@Service
public class RegisterService {

    private final RedisCache redisCache;
    private final SysUserMapper userMapper;
    private final SysLoginService loginService;
    private final MessageUtil messageUtil;

    public RegisterService(RedisCache redisCache, SysUserMapper userMapper, SysLoginService loginService, MessageUtil messageUtil) {
        this.redisCache = redisCache;
        this.userMapper = userMapper;
        this.loginService = loginService;
        this.messageUtil = messageUtil;
    }

    public void sendMessage(String phone) {
        //校验手机号输入是否正确
        if (!FgrUtil.checkMobileNumber(phone)){
            throw new ResultException("手机号输入不正确");
        }
        //获取六位验证码
        String code = FgrUtil.getCode();
        if (messageUtil.sendPhoneMessage(phone,code)){
            //根据手机号发送短信
            log.info("给{}发送验证码:{}",phone,code);
            //将验证码存入redis中 phone为key code为value 过期时间为五分钟
            redisCache.setCacheObject(phone,code,5, TimeUnit.MINUTES);
        } else {
            log.info("给{}发送验证码:{},失败",phone,code);
            throw new ResultException("短信发送失败");
        }

    }

    public String register(RegisterVo info) {
        String phone = info.getPhone();
        //校验验证码是否正确
        String captcha = redisCache.getCacheObject(phone);
        if (captcha == null){
            throw new ResultException("验证码过期");
        }
        if (!info.getCode().equalsIgnoreCase(captcha)){
            throw new ResultException("验证码错误");
        }
        //判断手机号是否已注册
        SysUserDo userDo = userMapper.selectOne(new LambdaQueryWrapper<SysUserDo>()
                .eq(SysUserDo::getPhonenumber, phone));
        if (ObjectUtil.isNull(userDo)){
            //用户不存在 注册用户
            //新增用户信息
            userDo = new SysUserDo();
            userDo.setUserName(phone);
            userDo.setNickName("phone_"+phone);
            userDo.setPhonenumber(phone);
            userDo.setStatus("0");
            userMapper.insert(userDo);
        }
        //用户已存在 直接登录
        String token = loginService.userLogin(userDo);
        //删除redis中缓存的验证码
        redisCache.deleteObject(phone);
        return token;

    }
}
