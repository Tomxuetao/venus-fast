package com.venus.modules.oauth.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.oauth.dto.SysClientDTO;
import com.venus.modules.oauth.entity.SysClientEntity;

import java.util.Map;

public interface SysClientService extends BaseService<SysClientEntity> {
    PageData<SysClientDTO> page(Map<String, Object> params);

    SysClientDTO get(Long id);

    void save(SysClientDTO dto);
}
