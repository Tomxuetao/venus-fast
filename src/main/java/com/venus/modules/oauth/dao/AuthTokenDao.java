package com.venus.modules.oauth.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.oauth.entity.AuthTokenEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthTokenDao extends BaseDao<AuthTokenEntity> {
}
