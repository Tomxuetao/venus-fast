package com.venus.modules.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "登录表单")
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "{sysuser.account.require}")
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "{sysuser.secretKey.require}")
    private String secretKey;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "{sysuser.captcha.require}")
    private String captcha;

    @ApiModelProperty(value = "唯一标识")
    @NotBlank(message = "{sysuser.uuid.require}")
    private String uuid;

}
