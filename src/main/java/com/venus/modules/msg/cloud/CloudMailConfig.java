package com.venus.modules.msg.cloud;

import com.venus.common.validator.group.AliyunGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CloudMailConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型 1：阿里云  2：腾讯云   3：七牛云")
    @Range(min = 1, max = 3, message = "{oss.type.range}")
    private Integer type;

    @ApiModelProperty(value = "阿里云Region ID")
    @NotBlank(message="{qcloud.region.require}", groups = AliyunGroup.class)
    private String aliyunRegion;

    @ApiModelProperty(value = "阿里云EndPoint")
    @NotBlank(message = "{aliyun.endPoint.require}", groups = AliyunGroup.class)
    private String aliyunEndPoint;

    @ApiModelProperty(value = "阿里云AccountName")
    @NotBlank(message = "{aliyun.accountName.require}", groups = AliyunGroup.class)
    private String aliyunAccountName;

    @ApiModelProperty(value = "阿里云AccessKeyId")
    @NotBlank(message = "{aliyun.accesskeyid.require}", groups = AliyunGroup.class)
    private String aliyunAccessKeyId;

    @ApiModelProperty(value = "阿里云AccessKeySecret")
    @NotBlank(message = "{aliyun.accesskeysecret.require}", groups = AliyunGroup.class)
    private String aliyunAccessKeySecret;

}
