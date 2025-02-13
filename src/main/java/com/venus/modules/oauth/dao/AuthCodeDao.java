package com.venus.modules.oauth.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.oauth.entity.AuthCodeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthCodeDao extends BaseDao<AuthCodeEntity> {
}
