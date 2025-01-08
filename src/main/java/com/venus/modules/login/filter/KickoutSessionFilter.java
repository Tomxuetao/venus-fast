package com.venus.modules.login.filter;

import com.venus.common.constant.Constant;
import com.venus.common.utils.SpringContextUtils;
import com.venus.common.utils.SseMsg;
import com.venus.modules.login.user.SecurityUser;
import com.venus.modules.login.user.UserDetail;
import com.venus.modules.sys.service.SysSseService;
import lombok.Setter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

@Setter
public class KickoutSessionFilter extends AccessControlFilter {
    private int maxSession = 1;

    private SysSseService sysSseService;
    private SessionManager sessionManager;

    private Cache<Long, Deque<Serializable>> cache;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        Subject subject = getSubject(request, response);
        // 如果没有登录或用户最大会话数为-1，直接进行之后的流程
        if(!subject.isAuthenticated() && !subject.isRemembered() || maxSession == -1) {
            return true;
        }

        Session session = subject.getSession();
        UserDetail user = SecurityUser.getUser();
        Long userId = user.getId();
        Serializable sessionId = session.getId();

        try {
            Deque<Serializable> deque = cache.get(userId);

            deque = deque == null ? new ArrayDeque<>() : deque;

            // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
            if(!deque.contains(sessionId)) {
                // 将sessionId存入队列
                deque.push(sessionId);
                // 将用户的sessionId队列缓存
                cache.put(userId, deque);
            }
            while (deque.size() > maxSession) {
                Serializable kickoutSessionId = deque.removeLast();
                // 踢出后再更新下缓存队列
                cache.put(userId, deque);
                // 获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    Subject kickoutSubject = new Subject.Builder().sessionId(kickoutSessionId).buildSubject();
                    if(kickoutSubject != null) {
                        kickoutSubject.logout();
                        saveRequest(request);
                        this.actionKickout(userId);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("踢出用户失败，原因：" + e.getMessage());
            this.actionKickout(userId);
        }
        return true;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(Constant.SYS_CACHE);
    }

    public void actionKickout(Long userId) {
        sysSseService = sysSseService == null ? SpringContextUtils.getBean(SysSseService.class) : sysSseService;
        SseMsg<String> msg = new SseMsg<>();
        msg.setType(0);
        msg.setBody("您已在别处登录，请您修改密码或重新登录");
        sysSseService.sendMsg(userId, msg);
    }
}
