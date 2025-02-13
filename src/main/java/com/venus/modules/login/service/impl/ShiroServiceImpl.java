package com.venus.modules.login.service.impl;

import com.venus.common.redis.RedisKeys;
import com.venus.common.redis.RedisUtils;
import com.venus.modules.login.dao.SysUserTokenDao;
import com.venus.modules.login.entity.SysUserTokenEntity;
import com.venus.modules.login.service.ShiroService;
import com.venus.modules.login.user.UserDetail;
import com.venus.modules.sys.dao.SysMenuDao;
import com.venus.modules.sys.dao.SysRoleDataScopeDao;
import com.venus.modules.sys.dao.SysUserDao;
import com.venus.modules.sys.entity.SysUserEntity;
import com.venus.modules.sys.enums.SuperAdminEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private SysRoleDataScopeDao sysRoleDataScopeDao;

    @Override
    public Set<String> getUserPermissions(UserDetail user) {
        //系统管理员，拥有最高权限
        List<String> permissionsList;
        if(user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            permissionsList = sysMenuDao.getPermissionsList();
        } else {
            permissionsList = sysMenuDao.getUserPermissionsList(user.getId());
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String permissions : permissionsList) {
            if(StringUtils.isBlank(permissions)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(permissions.trim().split(",")));
        }

        return permsSet;
    }

    @Override
    public SysUserTokenEntity getByToken(String token) {
        String key = RedisKeys.getTokenKey(token);
        SysUserTokenEntity userToken = redisUtils.get(key, SysUserTokenEntity.class);
        if(userToken == null) {
            userToken = sysUserTokenDao.getByToken(token);
            redisUtils.set(key, userToken, RedisUtils.DEFAULT_EXPIRE);
        }
        return userToken;
    }

    @Override
    public SysUserEntity getUser(Long userId) {
        String key = RedisKeys.getSecurityUserKey(userId);
        SysUserEntity user = redisUtils.get(key, SysUserEntity.class);
        if(user == null) {
            user = sysUserDao.getById(userId);
            redisUtils.set(key, user, RedisUtils.DEFAULT_EXPIRE);
        }
        return user;
    }

    @Override
    public List<Long> getDataScopeList(Long userId) {
        String key = RedisKeys.getDataScopeKey(userId);
        List<Long> dataScopeList = redisUtils.getList(key, Long.class);
        if(dataScopeList == null) {
            dataScopeList = sysRoleDataScopeDao.getDataScopeList(userId);
            redisUtils.set(key, dataScopeList, RedisUtils.DEFAULT_EXPIRE);
        }
        return dataScopeList;
    }
}
