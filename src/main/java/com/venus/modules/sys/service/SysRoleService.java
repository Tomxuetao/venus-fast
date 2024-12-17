package com.venus.modules.sys.service;

import com.venus.common.page.PageData;
import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysRoleDTO;
import com.venus.modules.sys.dto.SysUserDTO;
import com.venus.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends BaseService<SysRoleEntity> {
    PageData<SysRoleDTO> page(Map<String, Object> params);

    List<SysRoleDTO> list(Map<String, Object> params);

    SysRoleDTO get(Long id);

    void save(SysRoleDTO dto);

    void update(SysRoleDTO dto);

    void delete(Long[] ids);

    PageData<SysRoleDTO> getListByUserId(Map<String, Object> params);
}
