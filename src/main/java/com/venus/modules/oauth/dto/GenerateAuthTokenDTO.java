package com.venus.modules.oauth.dto;

import com.venus.common.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "生成AccessTokenDTO")
public class GenerateAuthTokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "code不能为空", groups = DefaultGroup.class)
    private String code;
    @NotBlank(message = "clientId不能为空", groups = DefaultGroup.class)
    private String clientId;
    @NotBlank(message = "redirectUri不能为空", groups = DefaultGroup.class)
    private String redirectUri;
    @NotBlank(message = "clientSecret不能为空", groups = DefaultGroup.class)
    private String clientSecret;

}
