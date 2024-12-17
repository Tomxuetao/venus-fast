package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {
    List<SysRoleEntity> getListByUserId(Map<String, Object> params);
}
