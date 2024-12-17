package com.venus.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.modules.sys.dao.SysRoleUserDao;
import com.venus.modules.sys.dto.SysUserDTO;
import com.venus.modules.sys.entity.SysRoleUserEntity;
import com.venus.modules.sys.service.SysRoleUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserDao, SysRoleUserEntity> implements SysRoleUserService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除角色用户关系
        deleteByUserIds(new Long[]{userId});

        //用户没有一个角色权限的情况
        if(CollUtil.isEmpty(roleIdList)) {
            return;
        }

        //保存角色用户关系
        for (Long roleId : roleIdList) {
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setUserId(userId);
            sysRoleUserEntity.setRoleId(roleId);

            //保存
            insert(sysRoleUserEntity);
        }
    }

    @Override
    public void deleteUserRoles(Long userId, List<Long> roleIdList) {
        QueryWrapper<SysRoleUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("role_id", roleIdList);
        baseDao.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Long userId, List<Long> roleIdList) {
        List<Long> curRoleIdList = getRoleIdList(userId);
        // 移除已存在的角色
        roleIdList.removeAll(curRoleIdList);
        if(roleIdList.isEmpty()){
            return;
        }
        List<SysRoleUserEntity> list = new ArrayList<>();
        for (Long roleId :roleIdList) {
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setUserId(userId);
            sysRoleUserEntity.setRoleId(roleId);

            list.add(sysRoleUserEntity);
        }
        insertBatch(list);
    }

    @Override
    public void deleteByRoleIds(Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }

    @Override
    public void deleteByUserIds(Long[] userIds) {
        baseDao.deleteByUserIds(userIds);
    }

    @Override
    public List<Long> getRoleIdList(Long userId) {

        return baseDao.getRoleIdList(userId);
    }

    @Override
    public List<Long> getUserIdsByRoleId(Long roleId) {
        QueryWrapper<SysRoleUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return baseDao.selectList(queryWrapper).stream().map(SysRoleUserEntity::getUserId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByRoleId(Long roleId, List<Long> userIdList) {
        List<SysRoleUserEntity> list = new ArrayList<>();
        for (Long userId : userIdList) {
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setRoleId(roleId);
            sysRoleUserEntity.setUserId(userId);

            list.add(sysRoleUserEntity);
        }
        insertBatch(list);
    }

    @Override
    public void deleteByRoleId(Long roleId, List<Long> userIdList) {
        QueryWrapper<SysRoleUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        queryWrapper.in("user_id", userIdList);
        baseDao.delete(queryWrapper);
    }

    @Override
    public Long countByRoleIds(Long[] roleIdList) {
        QueryWrapper<SysRoleUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", Arrays.asList(roleIdList));
        return baseDao.selectCount(queryWrapper);
    }
}
