package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.SysOnlineEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysOnlineDao extends BaseDao<SysOnlineEntity> {
    void deleteBySessionId(String sessionId);

    SysOnlineEntity selectBySessionId(String sessionId);

    List<SysOnlineEntity> selectListByExpired(Date expiredDate);
}
