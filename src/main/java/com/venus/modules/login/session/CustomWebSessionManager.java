package com.venus.modules.login.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.venus.common.utils.SpringContextUtils;
import com.venus.modules.sys.entity.SysOnlineEntity;
import com.venus.modules.sys.service.SysOnlineService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class CustomWebSessionManager extends DefaultWebSessionManager {
    private static final Logger log = LoggerFactory.getLogger(CustomWebSessionManager.class);

    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException {
        super.setAttribute(sessionKey, attributeKey, value);
        if(value != null && needMarkAttributeChanged(attributeKey)) {
            CustomSession customSession = getCustomSession(sessionKey);

            customSession.markAttributeChanged();
        }
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) {
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if(removed != null) {
            CustomSession customSession = getCustomSession(sessionKey);

            customSession.markAttributeChanged();
        }
        return removed;
    }

    public CustomSession getCustomSession(SessionKey sessionKey) {
        CustomSession customSession = null;
        System.out.println(sessionKey.getSessionId());
        Object o = doGetSession(sessionKey);
        if(o != null) {
            customSession = new CustomSession();
            BeanUtils.copyProperties(o, customSession);
        }
        System.out.println(customSession);
        return customSession;
    }

    @Override
    public void validateSessions() {
        if(log.isInfoEnabled()) {
            log.info("invalidation sessions...");
        }

        int invalidCount = 0;

        int timeout = (int) this.getGlobalSessionTimeout();
        if(timeout < 0) {
            return;
        }

        Date expiredDate = DateUtils.addMilliseconds(new Date(), -timeout);
        SysOnlineService sysOnlineService = SpringContextUtils.getBean(SysOnlineService.class);

        List<SysOnlineEntity> list = sysOnlineService.selectListByExpired(expiredDate);

        List<Long> needRemoveList = new ArrayList<>();

        for (SysOnlineEntity sysOnlineEntity : list) {
            SessionKey sessionKey = new DefaultSessionKey(sysOnlineEntity.getSessionId());
            try {
                Session session = retrieveSession(sessionKey);
                if(session != null) {
                    session.stop();
                    throw new InvalidSessionException();
                }
            } catch (InvalidSessionException e) {
                if(log.isDebugEnabled()) {
                    boolean expired = (e instanceof ExpiredSessionException);
                    String msg = "Invalidated session with id [" + sysOnlineEntity.getSessionId() + "]" + (expired ? " (expired)" : " (stopped)");
                    log.debug(msg);
                }
                invalidCount++;

                needRemoveList.add(sysOnlineEntity.getId());

                sysOnlineService.removeUserCache(sysOnlineEntity.getUserId(), sysOnlineEntity.getSessionId());
            }
        }

        if(!needRemoveList.isEmpty()) {
            sysOnlineService.deleteBatchIds(needRemoveList);
        }

        if(log.isInfoEnabled()) {
            String msg = "Finished invalidation session.";
            if(invalidCount > 0) {
                msg += " [" + invalidCount + "] sessions were stopped.";
            } else {
                msg += " No sessions were stopped.";
            }
            log.info(msg);
        }
    }

    @Override
    protected Collection<Session> getActiveSessions() {
        throw new UnsupportedOperationException("getActiveSessions method not supported");
    }

    private boolean needMarkAttributeChanged(Object attributeKey) {
        if(attributeKey == null) {
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        // 优化 flash属性没必要持久化
        if(attributeKeyStr.startsWith("org.springframework")) {
            return false;
        }
        if(attributeKeyStr.startsWith("javax.servlet")) {
            return false;
        }
        return !attributeKeyStr.equals("username");
    }
}
