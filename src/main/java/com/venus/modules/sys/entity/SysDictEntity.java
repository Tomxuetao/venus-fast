package com.venus.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict")
public class SysDictEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 字典类型ID
     */
    private Long pid;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典值
     */
    private String value;
    /**
     * 状态  0：停用   1：正常
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
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
