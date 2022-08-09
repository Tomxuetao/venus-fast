package com.venus.modules.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.constant.Constant;
import com.venus.common.page.PageData;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.job.dao.ScheduleJobLogDao;
import com.venus.modules.job.dto.ScheduleJobLogDTO;
import com.venus.modules.job.entity.ScheduleJobLogEntity;
import com.venus.modules.job.service.ScheduleJobLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScheduleJobLogServiceImpl extends BaseServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public PageData<ScheduleJobLogDTO> page(Map<String, Object> params) {
        IPage<ScheduleJobLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        return getPageData(page, ScheduleJobLogDTO.class);
    }

    private QueryWrapper<ScheduleJobLogEntity> getWrapper(Map<String, Object> params){
        String jobId = (String)params.get("jobId");

        QueryWrapper<ScheduleJobLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(jobId), "job_id", jobId);

        return wrapper;
    }

    @Override
    public ScheduleJobLogDTO get(Long id) {
        ScheduleJobLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScheduleJobLogDTO.class);
    }

}
