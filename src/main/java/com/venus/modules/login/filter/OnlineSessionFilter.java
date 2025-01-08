package com.venus.modules.login.filter;

import com.venus.common.constant.Constant;
import com.venus.modules.login.session.CustomSession;
import com.venus.modules.login.session.CustomSessionDAO;
import com.venus.modules.login.user.SecurityUser;
import com.venus.modules.login.user.UserDetail;
import lombok.Setter;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Setter
public class OnlineSessionFilter extends AccessControlFilter {

    private CustomSessionDAO customSessionDAO;

    /**
     * 是否允许访问
     *
     * @param request     the incoming <code>ServletRequest</code>
     * @param response    the outgoing <code>ServletResponse</code>
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);

        if(subject == null || subject.getSession() == null) {
            return true;
        }

        Session session = customSessionDAO.readSession(subject.getSession().getId());

        if(session instanceof CustomSession) {
            CustomSession customSession = (CustomSession) session;

            request.setAttribute(Constant.ONLINE_SESSION, customSession);

            boolean isGuest = customSession.getUserId() == null || customSession.getUserId() == 0L;

            if(isGuest) {
                UserDetail user = SecurityUser.getUser();
                customSession.setUserId(user.getId());
                customSession.setDeptId(user.getDeptId());
                customSession.setUsername(user.getUsername());
                customSession.markAttributeChanged();
            }

            return customSession.getStatus() != Constant.OnlineStatus.OFFLINE.getValue();
        }

        return true;
    }

    /**
     * 访问拒绝时调用
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(subject != null) {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }


}
