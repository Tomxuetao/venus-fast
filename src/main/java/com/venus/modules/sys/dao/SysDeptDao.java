package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysDeptDao extends BaseDao<SysDeptEntity> {
    List<SysDeptEntity> getList(Map<String, Object> params);

    SysDeptEntity getById(Long id);

    /**
     * 获取所有部门的id、pid列表
     */
    List<SysDeptEntity> getIdAndPidList();

    /**
     * 根据部门ID，获取所有子部门ID列表
     *
     * @param id 部门ID
     */
    List<Long> getSubDeptIdList(String id);
}
