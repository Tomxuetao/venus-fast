package com.venus.modules.sys.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.common.utils.Result;
import com.venus.modules.sys.entity.SysOnlineEntity;
import org.apache.shiro.session.Session;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SysOnlineService extends BaseService<SysOnlineEntity> {

    PageData<SysOnlineEntity> page(Map<String, Object> params);

    void deleteBySessionId(String sessionId);

    Session selectBySessionId(String sessionId);

    void saveOrUpdate(SysOnlineEntity entity);

    Result batchForceLogout(List<Long> ids);

    void removeUserCache(Long userId);

    List<SysOnlineEntity> selectListByExpired(Date expiredDate);
}
