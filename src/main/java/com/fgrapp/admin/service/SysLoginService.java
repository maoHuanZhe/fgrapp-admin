package com.fgrapp.admin.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.fgrapp.admin.domain.LoginBody;
import com.fgrapp.admin.domain.SysMenuDo;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.admin.domain.vo.RouterVo;
import com.fgrapp.base.config.redis.RedisCache;
import com.fgrapp.base.constant.Constants;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.constant.UserStatus;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.utils.FgrUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * SysLoginService
 *
 * @author fan guang rui
 * @date 2021年08月02日 18:23
 */
@Slf4j
@Service
public class SysLoginService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysLogininforService logininforService;
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Value("${fgr.captchTtype:char}")
    private String captchaType;
    @Value("${fgr.privateKey}")
    private String privateKey;

    @Transactional
    public String login(LoginBody loginBody) {
        //根据uuid 获取验证码答案
        String verifyKey = Constants.CAPTCHA_CODE_KEY + loginBody.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null){
            logininforService.saveLog(loginBody.getUsername(),Constants.FAIL,"验证码过期");
            throw new ResultException("验证码过期");
        }
        if (!loginBody.getCode().equalsIgnoreCase(captcha)){
            logininforService.saveLog(loginBody.getUsername(),Constants.FAIL,"验证码错误");
            throw new ResultException("验证码错误");
        }
        //校验用户名与密码
        String username = loginBody.getUsername();
        SysUserDo user = userService.selectUserByUserName(username);
        if (ObjectUtil.isNull(user)){
            log.info("登录用户：{} 不存在.", username);
            throw new ResultException("登录用户：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())){
            log.info("登录用户：{} 已被删除.", username);
            throw new ResultException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus())){
            log.info("登录用户：{} 已被停用.", username);
            throw new ResultException("对不起，您的账号：" + username + " 已停用");
        }
        //密码用私钥解密
        String password = loginBody.getPassword();
        RSA rsa = new RSA(privateKey,null);
        String decrypt = rsa.decryptStr(password, KeyType.PrivateKey);
        //将明文密码 对称加密 与数据库密码比较
        password = FgrUtil.encryptionPassword(decrypt, privateKey);
        if (!user.getPassword().equals(password)){
            log.info("登录用户：{} 密码输入错误.", username);
            logininforService.saveLog(loginBody.getUsername(),Constants.FAIL,"密码输入错误");
            throw new ResultException("密码输入错误");
        }
        String device = FgrUtil.getDevice();
        StpUtil.login(user.getId(),device);
        SaSession saSession = StpUtil.getTokenSession();
        recordLoginInfo(user);
        saSession.set(UserConstants.USER_KEY,user);
        logininforService.saveLog(loginBody.getUsername(),Constants.SUCCESS,"OK");
        return StpUtil.getTokenInfo().tokenValue;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUserDo user){
        user.setLoginIp(FgrUtil.getIpAddr(FgrUtil.getRequest()));
        user.setLoginDate(DateUtil.date());
        userService.updateById(user);
    }
    public Map<String,String> getCode() throws IOException {
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr, code = null;
        BufferedImage image = null;

        // 生成验证码
        if (Constants.CAPTCHATYPE_MATH.equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if (Constants.CAPTCHATYPE_CHAR.equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        if (image != null) {
            ImageIO.write(image, "jpg", os);
        }

        Map<String,String> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", Base64.encode(os.toByteArray()));
        return map;
    }

    public Map<String, Object> getInfo() {
        SysUserDo user = FgrUtil.getSysUserDo();
        // 角色集合
        List<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        List<String> permissions = permissionService.getMenuPermission(user);
        Map<String,Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roles", roles);
        map.put("permissions", permissions);
        return map;
    }

    public List<RouterVo> getRouters() {
        List<SysMenuDo> menus = menuService.selectMenuTreeByUserId(FgrUtil.getUserId());
        return menuService.buildMenus(menus);
    }

    public Void logout() {
        logininforService.saveLog(FgrUtil.getUsername(),Constants.SUCCESS,"退出成功");
        StpUtil.logout();
        return null;
    }
}
