package com.venus.modules.job.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.page.PageData;
import com.venus.modules.job.dto.ScheduleJobLogDTO;
import com.venus.modules.job.entity.ScheduleJobLogEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ScheduleJobLogService extends BaseService<ScheduleJobLogEntity> {
    PageData<ScheduleJobLogDTO> page(Map<String, Object> params);

    ScheduleJobLogDTO get(Long id);
}
