package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictDao extends BaseDao<SysDictEntity> {
    List<SysDictEntity> getListPids(Long[] pids);
}
