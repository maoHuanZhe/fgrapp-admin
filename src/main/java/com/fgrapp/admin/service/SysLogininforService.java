package com.fgrapp.admin.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fgrapp.admin.dao.SysLogininforMapper;
import com.fgrapp.admin.domain.SysLogininforDo;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import com.fgrapp.base.utils.PageUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * SysLogininforService
 *
 * @author fan guang rui
 * @date 2021年08月05日 12:39
 */
@Service
public class SysLogininforService extends FgrService<SysLogininforMapper, SysLogininforDo> {
    public void saveLog(String username, String loginStatus, String msg) {
        HttpServletRequest request = FgrUtil.getRequest();
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(userAgent);
        SysLogininforDo logininforDo = SysLogininforDo.builder().userName(username)
                .status(loginStatus)
                .loginTime(DateUtil.date())
                .msg(msg)
                .ipaddr(FgrUtil.getIpAddr(request))
                .browser(ua.getBrowser().toString())
                .os(ua.getOs().toString())
                .device(FgrUtil.getDevice())
                .build();
        if ("OK".equals(msg)){
            SaSession saSession = StpUtil.getTokenSession();
            saSession.set(UserConstants.LOGIN_INFO,logininforDo);
        }
        baseMapper.insert(logininforDo);
    }

    public IPage<List<Map<String, Object>>> getPage(Map<String, Object> map) {
        return baseMapper.getPage(PageUtil.getParamPage(map,SysLogininforDo.class),map);
    }
}
