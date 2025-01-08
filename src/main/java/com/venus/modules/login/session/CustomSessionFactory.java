package com.venus.modules.login.session;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.venus.common.utils.IpUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义sessionFactory
 */
@Component
public class CustomSessionFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext initData) {
        CustomSession session = new CustomSession();

        if(initData instanceof WebSessionContext) {
            WebSessionContext sessionContext = (WebSessionContext) initData;

            HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();

            if(request != null) {
                UserAgent userAgent = UserAgentUtil.parse(request.getHeader("user-agent"));
                session.setHost(IpUtils.getIpAddr(request));
                session.setOs(userAgent.getOs().toString());
                session.setBrowser(userAgent.getBrowser().toString());
            }
        }

        System.out.println(session);

        return session;
    }
}
