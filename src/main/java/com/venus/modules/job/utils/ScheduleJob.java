package com.venus.modules.job.utils;

import com.venus.common.constant.Constant;
import com.venus.common.exception.ExceptionUtils;
import com.venus.common.utils.SpringContextUtils;
import com.venus.modules.job.entity.ScheduleJobEntity;
import com.venus.modules.job.entity.ScheduleJobLogEntity;
import com.venus.modules.job.service.ScheduleJobLogService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;

public class ScheduleJob extends QuartzJobBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJobEntity scheduleJob = new ScheduleJobEntity();
        Object object = context.getMergedJobDataMap().get(ScheduleUtils.JOB_PARAM_KEY);
        BeanUtils.copyProperties(object, scheduleJob);

        //数据库保存执行记录
        ScheduleJobLogEntity log = new ScheduleJobLogEntity();
        log.setCreateDate(new Date());
        log.setJobId(scheduleJob.getId());
        log.setParams(scheduleJob.getParams());
        log.setBeanName(scheduleJob.getBeanName());

        //任务开始时间
        long startTime = System.currentTimeMillis();

        try {
            //执行任务
            logger.info("任务准备执行，任务ID：{}", scheduleJob.getId());
            Object target = SpringContextUtils.getBean(scheduleJob.getBeanName());
            Method method = target.getClass().getDeclaredMethod("run", String.class);
            method.invoke(target, scheduleJob.getParams());

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int) times);
            //任务状态
            log.setStatus(Constant.SUCCESS);

            logger.info("任务执行完毕，任务ID：{}  总共耗时：{} 毫秒", scheduleJob.getId(), times);
        } catch (Exception e) {
            logger.error("任务执行失败，任务ID：{}", scheduleJob.getId(), e);

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int) times);

            //任务状态
            log.setStatus(Constant.FAIL);
            log.setError(ExceptionUtils.getErrorStackTrace(e));
        } finally {
            //获取spring bean
            ScheduleJobLogService scheduleJobLogService = SpringContextUtils.getBean(ScheduleJobLogService.class);
            scheduleJobLogService.insert(log);
        }
    }
}
