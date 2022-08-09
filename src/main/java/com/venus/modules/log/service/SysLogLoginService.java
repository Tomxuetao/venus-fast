package com.venus.modules.log.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.log.dto.SysLogLoginDTO;
import com.venus.modules.log.entity.SysLogLoginEntity;

import java.util.List;
import java.util.Map;

public interface SysLogLoginService extends BaseService<SysLogLoginEntity> {

    PageData<SysLogLoginDTO> page(Map<String, Object> params);

    List<SysLogLoginDTO> list(Map<String, Object> params);

    void save(SysLogLoginEntity entity);
}
