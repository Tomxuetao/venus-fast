
package com.venus.modules.job.utils;

import com.venus.modules.job.entity.ScheduleJobEntity;
import com.venus.common.exception.RRException;
import com.venus.common.utils.Constant;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author Tomxuetao
 */
public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";

    /**
     * 获取触发器key
     */
    private static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    private static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("获取定时任务CronTrigger出现异常", e);
        }
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) {
        try {
        	//构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJobEntity.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression())
            		.withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJobEntity.getJobId())).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleJobEntity.JOB_PARAM_KEY, scheduleJobEntity);

            scheduler.scheduleJob(jobDetail, cronTrigger);

            //暂停任务
            if(scheduleJobEntity.getStatus() == Constant.ScheduleStatus.PAUSE.getValue()){
            	pauseJob(scheduler, scheduleJobEntity.getJobId());
            }
        } catch (SchedulerException e) {
            throw new RRException("创建定时任务失败", e);
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJobEntity.getJobId());

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression())
            		.withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = getCronTrigger(scheduler, scheduleJobEntity.getJobId());

            //按新的cronExpression表达式重新构建trigger
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            //参数
            cronTrigger.getJobDataMap().put(ScheduleJobEntity.JOB_PARAM_KEY, scheduleJobEntity);

            scheduler.rescheduleJob(triggerKey, cronTrigger);

            //暂停任务
            if(scheduleJobEntity.getStatus() == Constant.ScheduleStatus.PAUSE.getValue()){
            	pauseJob(scheduler, scheduleJobEntity.getJobId());
            }

        } catch (SchedulerException e) {
            throw new RRException("更新定时任务失败", e);
        }
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) {
        try {
        	//参数
        	JobDataMap dataMap = new JobDataMap();
        	dataMap.put(ScheduleJobEntity.JOB_PARAM_KEY, scheduleJobEntity);

            scheduler.triggerJob(getJobKey(scheduleJobEntity.getJobId()), dataMap);
        } catch (SchedulerException e) {
            throw new RRException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("暂停定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("删除定时任务失败", e);
        }
    }
}
