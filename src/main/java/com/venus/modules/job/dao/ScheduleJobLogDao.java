package com.venus.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venus.modules.job.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author Tomxuetao
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {

}
