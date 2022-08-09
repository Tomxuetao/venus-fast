package com.venus.modules.log.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.log.dto.SysLogErrorDTO;
import com.venus.modules.log.entity.SysLogErrorEntity;

import java.util.List;
import java.util.Map;

public interface SysLogErrorService extends BaseService<SysLogErrorEntity> {

    PageData<SysLogErrorDTO> page(Map<String, Object> params);

    List<SysLogErrorDTO> list(Map<String, Object> params);

    void save(SysLogErrorEntity entity);

}
