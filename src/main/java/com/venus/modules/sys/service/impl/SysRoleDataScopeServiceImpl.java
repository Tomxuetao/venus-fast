package com.venus.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.modules.sys.dao.SysRoleDataScopeDao;
import com.venus.modules.sys.entity.SysRoleDataScopeEntity;
import com.venus.modules.sys.service.SysRoleDataScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleDataScopeServiceImpl extends BaseServiceImpl<SysRoleDataScopeDao, SysRoleDataScopeEntity> implements SysRoleDataScopeService {
    @Override
    public List<Long> getDeptIdList(Long roleId) {
        return baseDao.getDeptIdList(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        //先删除角色数据权限关系
        deleteByRoleIds(new Long[]{roleId});

        //角色没有一个数据权限的情况
        if (CollUtil.isEmpty(deptIdList)) {
            return;
        }

        //保存角色数据权限关系
        for (Long deptId : deptIdList) {
            SysRoleDataScopeEntity sysRoleDataScopeEntity = new SysRoleDataScopeEntity();
            sysRoleDataScopeEntity.setDeptId(deptId);
            sysRoleDataScopeEntity.setRoleId(roleId);

            //保存
            insert(sysRoleDataScopeEntity);
        }
    }

    @Override
    public void deleteByRoleIds(Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }

}
