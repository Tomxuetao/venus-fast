package com.venus.modules.log.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.log.dto.SysLogOperateDTO;
import com.venus.modules.log.entity.SysLogOperateEntity;

import java.util.List;
import java.util.Map;

public interface SysLogOperateService extends BaseService<SysLogOperateEntity> {
    PageData<SysLogOperateDTO> page(Map<String, Object> params);

    List<SysLogOperateDTO> list(Map<String, Object> params);

    void save(SysLogOperateEntity entity);
}
