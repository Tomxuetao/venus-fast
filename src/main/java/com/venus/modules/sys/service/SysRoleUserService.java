package com.venus.modules.sys.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.sys.dto.SysUserDTO;
import com.venus.modules.sys.entity.SysRoleUserEntity;

import java.util.List;
import java.util.Map;

public interface SysRoleUserService extends BaseService<SysRoleUserEntity> {
    /**
     * 根据角色ids，查询用户数量
     * @param roleIdList 角色ID列表
     */
    Long countByRoleIds(Long[] roleIdList);
    /**
     * 保存或修改
     *
     * @param userId     用户ID
     * @param roleIdList 角色ID列表
     */
    void saveOrUpdate(Long userId, List<Long> roleIdList);

    /**
     * 新增用户角色关系
     * @param userId 用户ID
     * @param roleIdList 角色ID列表
     */
    void saveUserRoles(Long userId, List<Long> roleIdList);

    /**
     * 删除用户角色关系
     * @param userId 用户ID
     * @param roleIdList 角色ID列表
     */
    void deleteUserRoles(Long userId, List<Long> roleIdList);

    /**
     * 根据角色ids，删除角色用户关系
     *
     * @param roleIds 角色ids
     */
    void deleteByRoleIds(Long[] roleIds);

    /**
     * 根据用户id，删除角色用户关系
     *
     * @param userIds 用户ids
     */
    void deleteByUserIds(Long[] userIds);

    /**
     * 角色ID列表
     *
     * @param userId 用户ID
     */
    List<Long> getRoleIdList(Long userId);

    /**
     * 根据角色ID，获取用户ID列表
     * @param roleId 角色ID
     */
    List<Long> getUserIdsByRoleId(Long roleId);

    void saveByRoleId(Long roleId, List<Long> userIdList);

    void deleteByRoleId(Long roleId, List<Long> userIdList);
}
