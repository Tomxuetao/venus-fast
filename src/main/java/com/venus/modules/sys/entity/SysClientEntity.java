package com.venus.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("sys_client")
@EqualsAndHashCode(callSuper = true)
public class SysClientEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用类型 0：PC端 1：移动端
     */
    private Integer appType;
    /**
     * 应用图标
     */
    private String appIcon;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 回调地址
     */
    private String redirectUri;
    /**
     * 应用分类
     */
    private Integer classify;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态  0：禁用   1：启用
     */
    private Integer status;
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
