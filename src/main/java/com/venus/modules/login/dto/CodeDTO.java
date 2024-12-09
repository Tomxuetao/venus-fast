package com.venus.modules.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "获取验证码表单")
public class CodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户账号-手机号或邮箱账号", required = true)
    @NotBlank(message = "{sysuser.account.require}")
    private String account;

    @ApiModelProperty(value = "图片验证码", required = true)
    @NotBlank(message = "{sysuser.captcha.require}")
    private String captcha;

    @ApiModelProperty(value = "唯一标识")
    @NotBlank(message = "{sysuser.uuid.require}")
    private String uuid;
}
