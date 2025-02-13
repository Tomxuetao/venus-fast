package com.venus.modules.oauth.service;

import com.venus.common.base.service.BaseService;
import com.venus.modules.oauth.entity.AuthCodeEntity;

public interface AuthCodeService extends BaseService<AuthCodeEntity> {
    String generateCode(Long userId, String clientId);

    AuthCodeEntity validateCode(String code, String clientId);
}
