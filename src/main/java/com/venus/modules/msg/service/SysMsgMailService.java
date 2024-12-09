package com.venus.modules.msg.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.msg.dto.SysMsgMailDTO;
import com.venus.modules.msg.entity.SysMsgMailEntity;

import java.util.List;
import java.util.Map;

public interface SysMsgMailService extends BaseService<SysMsgMailEntity> {
    PageData<SysMsgMailDTO> page(Map<String, Object> params);

    List<SysMsgMailDTO> list(Map<String, Object> params);

    void save(SysMsgMailEntity entity);
}
