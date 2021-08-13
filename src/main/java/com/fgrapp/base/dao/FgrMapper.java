package com.fgrapp.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FgrMapper
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:13
 */
public interface FgrMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入
     * @param batchList 插入列表
     * @return 插入数量
     */
    int insertBatch(@Param("list") List<T> batchList);
}
