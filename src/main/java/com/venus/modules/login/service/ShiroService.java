package com.venus.modules.login.service;

import com.venus.modules.login.entity.SysUserTokenEntity;
import com.venus.modules.login.user.UserDetail;
import com.venus.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Set;

public interface ShiroService {
    Set<String> getUserPermissions(UserDetail user);

    SysUserTokenEntity getByToken(String token);

    /**
     * 根据用户ID，查询用户
     *
     * @param userId 用户ID
     */
     SysUserEntity getUser(Long userId);

    /**
     * 获取用户对应的部门数据权限
     *
     * @param userId 用户ID
     * @return 返回部门ID列表
     */
    List<Long> getDataScopeList(Long userId);
}
