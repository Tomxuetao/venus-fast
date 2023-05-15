package com.venus.modules.sys.service;

import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysDeptDTO;
import com.venus.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

public interface SysDeptService extends BaseService<SysDeptEntity> {
    List<SysDeptDTO> list(Map<String, Object> params);

    SysDeptDTO get(Long id);

    void save(SysDeptDTO dto);

    void update(SysDeptDTO dto);

    void delete(Long id);

    /**
     * 根据部门ID，获取本部门及子部门ID列表
     *
     * @param id 部门ID
     */
    List<Long> getSubDeptIdList(Long id);
}
