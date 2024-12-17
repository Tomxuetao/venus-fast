package com.venus.modules.sys.service;

import com.venus.common.page.PageData;
import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysDictDTO;
import com.venus.modules.sys.entity.SysDictEntity;

import java.util.List;
import java.util.Map;

public interface SysDictService extends BaseService<SysDictEntity> {
    PageData<SysDictDTO> page(Map<String, Object> params);

    SysDictDTO get(Long id);

    List<SysDictDTO> list(Map<String, Object> params);

    void save(SysDictDTO dto);

    void update(SysDictDTO dto);

    void delete(Long[] ids);

    Long countByPids(Long[] pidList);
}
