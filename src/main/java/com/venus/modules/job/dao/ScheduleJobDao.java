package com.venus.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venus.common.base.dao.BaseDao;
import com.venus.modules.job.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 定时任务
 *
 * @author Tomxuetao
 */
@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {

    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);
}
