package com.venus.modules.job.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.venus.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务
 *
 * @author Tomxuetao
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("schedule_job")
public class ScheduleJobEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * spring bean名称
     */
    private String beanName;
    /**
     * 参数
     */
    private String params;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 任务状态  0：暂停  1：正常
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
