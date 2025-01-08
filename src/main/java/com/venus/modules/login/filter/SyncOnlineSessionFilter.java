package com.venus.modules.login.filter;

import com.venus.common.constant.Constant;
import com.venus.modules.login.session.CustomSession;
import com.venus.modules.login.session.CustomSessionDAO;
import lombok.Setter;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Setter
public class SyncOnlineSessionFilter extends PathMatchingFilter {

    public CustomSessionDAO customSessionDAO;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        CustomSession customSession = (CustomSession) request.getAttribute(Constant.ONLINE_SESSION);
        if(customSession != null && customSession.getUserId() != null && customSession.getStopTimestamp() == null) {
            customSessionDAO.syncToDb(customSession);
        }
        return true;
    }

}
