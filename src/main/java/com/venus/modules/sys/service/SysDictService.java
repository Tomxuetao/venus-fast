package com.venus.modules.sys.service;

import com.venus.common.page.PageData;
import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysDictDTO;
import com.venus.modules.sys.entity.SysDictEntity;

import java.util.Map;

public interface SysDictService extends BaseService<SysDictEntity> {
    PageData<SysDictDTO> page(Map<String, Object> params);

    SysDictDTO get(Long id);

    void save(SysDictDTO dto);

    void update(SysDictDTO dto);

    void delete(Long[] ids);
}
