package com.fgrapp.admin.domain.vo.server;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.fgrapp.base.utils.FgrUtil;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * JVM相关信息
 *
 * @author fan guang rui
 * @date 2021年06月11日 16:34
 */
@Data
public class Jvm
{
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal()
    {
        return FgrUtil.div(total, (1024 * 1024), 2);
    }


    public double getMax()
    {
        return FgrUtil.div(max, (1024 * 1024), 2);
    }


    public double getFree()
    {
        return FgrUtil.div(free, (1024 * 1024), 2);
    }


    public double getUsed()
    {
        return FgrUtil.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage()
    {
        return FgrUtil.mul(FgrUtil.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName()
    {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    /**
     * JDK启动时间
     */
    public String getStartTime(){
        return DateUtil.formatDateTime(FgrUtil.getServerStartDate());
    }

    /**
     * JDK运行时间
     */
    public String getRunTime(){
        long between = DateUtil.between(DateUtil.date(), FgrUtil.getServerStartDate(), DateUnit.SECOND);
        System.out.println(between);
        return DateUtil.formatBetween(between, BetweenFormatter.Level.MINUTE);
    }
}
