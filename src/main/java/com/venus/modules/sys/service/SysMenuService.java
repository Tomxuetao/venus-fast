package com.venus.modules.sys.service;

import com.venus.common.base.service.BaseService;
import com.venus.modules.security.user.UserDetail;
import com.venus.modules.sys.dto.SysMenuDTO;
import com.venus.modules.sys.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuService extends BaseService<SysMenuEntity> {
    SysMenuDTO get(Long id);

    void save(SysMenuDTO dto);

    void update(SysMenuDTO dto);

    void delete(Long id);

    /**
     * 菜单列表
     *
     * @param type 菜单类型
     */
    List<SysMenuDTO> getAllMenuList(Integer type);

    /**
     * 用户菜单列表
     *
     * @param user  用户
     * @param type 菜单类型
     */
    List<SysMenuDTO> getUserMenuList(UserDetail user, Integer type);

    /**
     * 根据父菜单，查询子菜单
     * @param pid  父菜单ID
     */
    List<SysMenuDTO> getListPid(Long pid);
}
