package com.venus.modules.sys.service;

import com.venus.common.page.PageData;
import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysDictTypeDTO;
import com.venus.modules.sys.entity.DictType;
import com.venus.modules.sys.entity.SysDictTypeEntity;

import java.util.List;
import java.util.Map;

public interface SysDictTypeService extends BaseService<SysDictTypeEntity> {
    PageData<SysDictTypeDTO> page(Map<String, Object> params);

    SysDictTypeDTO get(Long id);

    void save(SysDictTypeDTO dto);

    void update(SysDictTypeDTO dto);

    void delete(Long[] ids);

    /**
     * 获取所有字典
     */
    List<DictType> getAllList();
}
