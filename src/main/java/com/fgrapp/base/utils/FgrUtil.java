package com.fgrapp.base.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.fgrapp.admin.domain.SysConfigDo;
import com.fgrapp.admin.domain.SysUserDo;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.domain.BaseDo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * FgrUtil
 *
 * @author fan guang rui
 * @date 2021年08月01日 9:18
 */
@Slf4j
public class FgrUtil {
    public static String encryptionPassword(String decrypt, String privateKey) {
        String password;
        byte[] bytes = Arrays.copyOf(StrUtil.bytes(privateKey, CharsetUtil.UTF_8), 32);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES,bytes);
        password = aes.encryptHex(decrypt, CharsetUtil.CHARSET_UTF_8);
        return password;
    }
    public static boolean checkUnique(BaseDo info, List<? extends BaseDo> list) {
        int size = list.size();
        Long id = info.getId();
        if (size == 0){
            return false;
        } else if (size == 1){
            if (id != null){
                return !id.equals(list.get(0).getId());
            }
        }
        return true;
    }
    public static String getUsername() {
        SysUserDo user = getSysUserDo();
        if (ObjectUtil.isEmpty(user)){
            return "NoLogin";
        }
        return user.getUserName();
    }
    public static Long getUserId() {
        SysUserDo user = getSysUserDo();
        return user.getId();
    }

    public static SysUserDo getSysUserDo() {
        return StpUtil.getTokenSession().getModel(UserConstants.USER_KEY, SysUserDo.class);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }
    public static ServletRequestAttributes getRequestAttributes(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }
    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = BigDecimal.ONE;
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        if (b1.compareTo(BigDecimal.ZERO) == 0)
        {
            return BigDecimal.ZERO.doubleValue();
        }
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    public static String getHostIp(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
        }
        return "127.0.0.1";
    }

    public static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
        }
        return "未知";
    }
    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
    public static String getBodyString(ServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try (InputStream inputStream = request.getInputStream())
        {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            log.warn("getBodyString出现问题！");
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        }
        return sb.toString();
    }
    public static String getIpAddr(HttpServletRequest request)
    {
        if (request == null)
        {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : clean(ip);
    }
    /**
     * 清除所有HTML标签，但是不删除标签内的内容
     *
     * @param content 文本
     * @return 清除标签后的文本
     */
    public static String clean(String content)
    {
        return new HTMLFilter().filter(content);
    }

    public static String getDevice() {
        HttpServletRequest request = getRequest();
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("Android")||userAgent.contains("iPhone")) {
                return "mobile";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Mac")) {
            return "Mac";
        } else if(userAgent.contains("Windows")) {
            return "Windows";
        }
        return "NULL";
    }

    /**
     * 判断用户是否是超级管理员
     * @param sysUserDo
     * @return
     */
    public static boolean isAdmin(SysUserDo sysUserDo) {
        Long[] roleIds = sysUserDo.getRoleIds();
        for (Long roleId : roleIds) {
            if ("1".equals(roleId.toString())){
                return true;
            }
        }
        return false;
    }
}
