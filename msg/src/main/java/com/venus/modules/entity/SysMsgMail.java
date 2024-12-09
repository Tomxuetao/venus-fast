package com.venus.modules.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 邮件发送日志
 * </p>
 *
 * @author Tomxuetao
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_msg_mail")
@ApiModel(value="SysMsgMail对象", description="邮件发送日志")
public class SysMsgMail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "邮件模版ID")
    @TableField("temp_id")
    private Long tempId;

    @ApiModelProperty(value = "收件人")
    @TableField("recipient")
    private String recipient;

    @ApiModelProperty(value = "邮件主题")
    @TableField("subject")
    private String subject;

    @ApiModelProperty(value = "邮件内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "状态  0：失败   1：成功")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "用户名")
    @TableField("creator_name")
    private String creatorName;

    @ApiModelProperty(value = "创建者")
    @TableField("creator")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private LocalDateTime createDate;


}
