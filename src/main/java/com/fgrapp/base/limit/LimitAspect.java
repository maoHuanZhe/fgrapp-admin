package com.fgrapp.base.limit;

import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.utils.FgrUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * LimitAspect
 *
 * @author fan guang rui
 * @date 2021年09月12日 19:11
 */
//@Aspect
//@Configuration
//@Order(1)
public class LimitAspect {
    //根据IP分不同的令牌桶，每天自动清理缓存
    private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
            .maximumWeight(1000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String s) throws Exception {
                    // 新的IP初始化 每分钟只发出5个令牌
                    return RateLimiter.create(0.5);
                }
            });
    //限流切点
    @Pointcut("@annotation(com.fgrapp.base.limit.ServiceLimit)")
    public void ServiceAspect(){}

    @Around("ServiceAspect()")
    public Object around(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ServiceLimit limitAnnotation = method.getAnnotation(ServiceLimit.class);
        ServiceLimit.LimitType limitType = limitAnnotation.limitType();
        String key = limitAnnotation.key();
        Object obj;
        try {
            if (limitType.equals(ServiceLimit.LimitType.IP)){
                key = FgrUtil.getHostIp();
            }
            RateLimiter rateLimiter = caches.get(key);
            boolean flag = rateLimiter.tryAcquire();
            if (flag) {
                obj = joinPoint.proceed();
            } else {
                throw new ResultException("小同志，你访问的太频繁了");
            }
        } catch (Throwable throwable) {
            throw new ResultException("小同志，你访问的太频繁了");
        }
        return obj;
    }
}
