package com.venus.modules.sys.dao;

import com.venus.common.base.dao.BaseDao;
import com.venus.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {

    List<SysUserEntity> getList(Map<String, Object> params);

    SysUserEntity getById(Long id);

    SysUserEntity getByUsername(String username);

    void updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

    /**
     * 根据部门ID，查询用户数
     */
    int getCountByDeptId(Long deptId);

    /**
     * 根据部门ID,查询用户ID列表
     */
    List<Long> getUserIdListByDeptId(List<Long> deptIdList);
}
