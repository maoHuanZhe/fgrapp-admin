package com.fgrapp.admin.domain.vo.server;


import com.fgrapp.base.utils.FgrUtil;
import lombok.Data;

/**
 * 內存相关信息
 *
 * @author fan guang rui
 * @date 2021年06月11日 16:34
 */
@Data
public class Mem
{
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public double getTotal()
    {
        return FgrUtil.div(total, (1024 * 1024 * 1024), 2);
    }


    public double getUsed()
    {
        return FgrUtil.div(used, (1024 * 1024 * 1024), 2);
    }


    public double getFree()
    {
        return FgrUtil.div(free, (1024 * 1024 * 1024), 2);
    }


    public double getUsage()
    {
        return FgrUtil.mul(FgrUtil.div(used, total, 4), 100);
    }
}
