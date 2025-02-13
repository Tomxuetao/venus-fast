package com.venus.modules.oauth.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.oauth.entity.SysClientEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysClientDao extends BaseDao<SysClientEntity> {
}
