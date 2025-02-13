package com.venus.modules.oauth.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "AccessTokenDTO")
public class AuthTokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date expireDate;
    private String accessToken;
    private String refreshToken;
}
