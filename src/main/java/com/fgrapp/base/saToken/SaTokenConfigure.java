package com.fgrapp.base.saToken;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

/**
 * SaTokenConfigure
 *
 * @author fan guang rui
 * @date 2021年08月04日 12:55
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    /**
     * 注册Sa-Token的拦截器
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
        registry.addInterceptor(new SaAnnotationInterceptor()).excludePathPatterns( Arrays.asList("/login", "/captchaImage", "/logout","/page/*")).addPathPatterns("/**");
    }
}

