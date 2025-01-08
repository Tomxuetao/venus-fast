package com.venus.modules.login.session;

import com.venus.common.constant.Constant;
import com.venus.modules.sys.entity.SysOnlineEntity;
import com.venus.modules.sys.service.SysOnlineService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

public class CustomSessionDAO extends EnterpriseCacheSessionDAO {

    @Autowired
    private SysOnlineService sysOnlineService;

    public CustomSessionDAO() {
        super();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return sysOnlineService.selectBySessionId(String.valueOf(sessionId));
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        super.update(session);
    }

    @Override
    protected void doDelete(Session session) {
        CustomSession customSession = (CustomSession) session;
        if(customSession == null) {
            return;
        }
        customSession.setStatus(Constant.OnlineStatus.OFFLINE.getValue());

        sysOnlineService.deleteBySessionId(String.valueOf(customSession.getId()));
    }

    public void syncToDb(CustomSession customSession) {
        Date lastSyncTimestamp = (Date) customSession.getAttribute(Constant.LAST_SYNC_DB_TIMESTAMP);

        if(lastSyncTimestamp != null) {
            boolean needSync = true;
            long deltaTime = customSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();

            if(deltaTime < 60 * 1000) {
                needSync = false;
            }

            boolean isGuest = customSession.getUserId() == null || customSession.getUserId() == 0L;
            if(!isGuest && customSession.isAttributeChanged()) {
                needSync = true;
            }

            if(!needSync) {
                return;
            }
        }

        customSession.setAttribute(Constant.LAST_SYNC_DB_TIMESTAMP, customSession.getLastAccessTime());

        if(customSession.isAttributeChanged()) {
            customSession.resetAttributeChanged();
        }

        SysOnlineEntity sysOnlineEntity = getSysOnlineEntity(customSession);

        sysOnlineService.saveOrUpdate(sysOnlineEntity);
    }

    @NotNull
    private static SysOnlineEntity getSysOnlineEntity(CustomSession customSession) {
        SysOnlineEntity sysOnlineEntity = new SysOnlineEntity();

        sysOnlineEntity.setSessionId(String.valueOf(customSession.getId()));

        sysOnlineEntity.setOs(customSession.getOs());
        sysOnlineEntity.setIp(customSession.getHost());
        sysOnlineEntity.setDeptId(customSession.getDeptId());
        sysOnlineEntity.setStatus(customSession.getStatus());
        sysOnlineEntity.setUserId(customSession.getUserId());
        sysOnlineEntity.setBrowser(customSession.getBrowser());
        sysOnlineEntity.setUsername(customSession.getUsername());
        sysOnlineEntity.setExpireTime(customSession.getTimeout());
        sysOnlineEntity.setUpdateDate(customSession.getLastAccessTime());

        return sysOnlineEntity;
    }
}
