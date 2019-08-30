package com.venus.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venus.common.utils.PageUtils;
import com.venus.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author Tomxuetao
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
