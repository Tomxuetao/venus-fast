package com.venus.modules.sys.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.sys.dto.SysClientDTO;
import com.venus.modules.sys.entity.SysClientEntity;

import java.util.Map;

public interface SysClientService extends BaseService<SysClientEntity> {
    PageData<SysClientDTO> page(Map<String, Object> params);

    SysClientEntity get(Long id);

    void save(SysClientDTO dto);

    void update(SysClientDTO dto);

    SysClientEntity validateClient(String clientId, String clientSecret);
}
