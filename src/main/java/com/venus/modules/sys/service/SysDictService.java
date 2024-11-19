package com.venus.modules.sys.service;

import com.venus.common.page.PageData;
import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysDictDataDTO;
import com.venus.modules.sys.entity.SysDictDataEntity;

import java.util.Map;

public interface SysDictDataService extends BaseService<SysDictDataEntity> {
    PageData<SysDictDataDTO> page(Map<String, Object> params);

    SysDictDataDTO get(Long id);

    void save(SysDictDataDTO dto);

    void update(SysDictDataDTO dto);

    void delete(Long[] ids);
}
