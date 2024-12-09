package com.venus.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.venus.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_msg_mail")
public class SysMsgMailEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件模版ID
     */
    private Long tempId;
    /**
     * 收件人
     */
    private String recipient;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 状态  0：失败   1：成功
     */
    private Integer status;

    /**
     * 发送者名称
     */
    private String creatorName;
}
