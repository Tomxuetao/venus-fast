package com.venus.modules.oauth.service;

import com.venus.common.base.service.BaseService;
import com.venus.modules.oauth.entity.AuthTokenEntity;

public interface AuthTokenService extends BaseService<AuthTokenEntity> {

    void logout(String token);

    AuthTokenEntity validateToken(String token);

    AuthTokenEntity generateToken(Long userId, String clientId);
}
