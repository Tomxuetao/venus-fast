package com.venus.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 文件上传
 *
 * @author Tomxuetao
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_oss")
public class SysOssEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String url;
    private String name;
}
