package com.venus.modules.msg.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "邮件发送记录")
public class SysMsgMailDTO {
    /**
     * id
     */
    private Long id;
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
     * 用户名
     */
    private String creatorName;
    /**
     * 创建者
     */
    private Long creator;
    /**
     * 创建时间
     */
    private Date createDate;
}
