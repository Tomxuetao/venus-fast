package com.venus.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.common.utils.SpringContextUtils;
import com.venus.common.utils.SseMsg;
import com.venus.modules.login.service.SysUserTokenService;
import com.venus.modules.login.session.CustomSession;
import com.venus.modules.login.user.SecurityUser;
import com.venus.modules.login.user.UserDetail;
import com.venus.modules.sys.dao.SysOnlineDao;
import com.venus.modules.sys.entity.SysOnlineEntity;
import com.venus.modules.sys.enums.SuperAdminEnum;
import com.venus.modules.sys.service.SysDeptService;
import com.venus.modules.sys.service.SysOnlineService;
import com.venus.modules.sys.service.SysSseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;
import java.util.*;

@Service
public class SysOnlineServiceImpl extends BaseServiceImpl<SysOnlineDao, SysOnlineEntity> implements SysOnlineService {

    @Autowired
    private SysSseService sysSseService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public PageData<SysOnlineEntity> page(Map<String, Object> params) {

        IPage<SysOnlineEntity> page = baseDao.selectPage(getPage(params, Constant.CREATE_DATE, false), getWrapper(params));

        return getPageData(page, SysOnlineEntity.class);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        baseDao.deleteBySessionId(sessionId);
    }

    @Override
    public Session selectBySessionId(String sessionId) {
        SysOnlineEntity sysOnlineEntity = baseDao.selectBySessionId(sessionId);

        if(sysOnlineEntity == null) {
            return null;
        } else {
            Date updateDate = sysOnlineEntity.getUpdateDate();

            if(updateDate.getTime() + sysOnlineEntity.getExpireTime() > System.currentTimeMillis()) {
                return null;
            } else {
                return createSession(sysOnlineEntity);
            }
        }
    }

    @Override
    public void saveOrUpdate(SysOnlineEntity entity) {
        SysOnlineEntity sysOnlineEntity = baseDao.selectBySessionId(entity.getSessionId());
        if(sysOnlineEntity == null) {
            baseDao.insert(entity);
        } else {
            entity.setStatus(Constant.OnlineStatus.ONLINE.getValue());
            baseDao.update(entity, new UpdateWrapper<SysOnlineEntity>().eq("session_id", entity.getSessionId()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchForceLogout(List<Long> ids) {
        List<SysOnlineEntity> list = baseDao.selectBatchIds(ids);

        String curSessionId = String.valueOf(SecurityUtils.getSubject().getSession().getId());

        for (SysOnlineEntity sysOnlineEntity : list) {
            if(curSessionId.equals(sysOnlineEntity.getSessionId())) {
                return new Result().error("不能退出自己");
            }
        }

        SseMsg<String> msg = new SseMsg<>(0, "你已被管理员强制下线");
        // 更新用户Token、删除用户缓存、发送消息
        for (SysOnlineEntity sysOnlineEntity : list) {
            Long userId = sysOnlineEntity.getUserId();
            sysUserTokenService.logout(userId);
            sysSseService.sendMsg(userId, msg);
            sysOnlineEntity.setStatus(Constant.OnlineStatus.OFFLINE.getValue());
            baseDao.updateById(sysOnlineEntity);
        }

        DefaultWebSessionManager sessionManager = SpringContextUtils.getBean(DefaultWebSessionManager.class);
        sessionManager.validateSessions();

        return new Result<>().ok("强制退出成功");
    }

    @Override
    public void removeUserCache(Long userId, String sessionId) {
        EhCacheManager ehCacheManager = SpringContextUtils.getBean(EhCacheManager.class);

        Cache<Long, SseEmitter> emitterCache = ehCacheManager.getCache(Constant.SYS_SSE_USER_CACHE);
        if(emitterCache.get(userId) != null) {
            emitterCache.remove(userId);
        }
        Cache<Long, Deque<Serializable>> cache = ehCacheManager.getCache(Constant.SYS_ONLINE_USER_CACHE);
        Deque<Serializable> deque = cache.get(userId);

        if(deque == null || deque.isEmpty()) {
            return;
        }

        deque.remove(sessionId);
    }

    @Override
    public List<SysOnlineEntity> selectListByExpired(Date expiredDate) {
        return baseDao.selectListByExpired(expiredDate);
    }

    public Session createSession(SysOnlineEntity sysOnlineEntity) {
        CustomSession customSession = new CustomSession();

        if(sysOnlineEntity != null) {
            customSession.setOs(sysOnlineEntity.getOs());
            customSession.setHost(sysOnlineEntity.getIp());
            customSession.setId(sysOnlineEntity.getSessionId());
            customSession.setDeptId(sysOnlineEntity.getDeptId());
            customSession.setUserId(sysOnlineEntity.getUserId());
            customSession.setBrowser(sysOnlineEntity.getBrowser());
            customSession.setUsername(sysOnlineEntity.getUsername());
        }

        return customSession;
    }

    private QueryWrapper<SysOnlineEntity> getWrapper(Map<String, Object> params) {
        String username = (String) params.get("username");

        QueryWrapper<SysOnlineEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", Constant.OnlineStatus.ONLINE.getValue());
        wrapper.like(StringUtils.isNotBlank(username), "username", username);

        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            List<Long> deptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            wrapper.in(deptIdList != null, "dept_id", deptIdList);
        }

        return wrapper;
    }
}
