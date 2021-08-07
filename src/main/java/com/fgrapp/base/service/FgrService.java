package com.fgrapp.base.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fgrapp.base.dao.FgrMapper;

/**
 * FgrService
 *
 * @author fan guang rui
 * @date 2021年07月31日 10:14
 */
public abstract class FgrService<M extends FgrMapper<T>,T> extends ServiceImpl<M,T> {

}
