package com.venus.modules.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.modules.oauth.dao.AuthCodeDao;
import com.venus.modules.oauth.entity.AuthCodeEntity;
import com.venus.modules.oauth.service.AuthCodeService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AuthCodeServiceImpl extends BaseServiceImpl<AuthCodeDao, AuthCodeEntity> implements AuthCodeService {
    @Override
    public String generateCode(Long userId, String clientId) {
        String code = generateSecureRandomCode();
        AuthCodeEntity authCodeEntity = new AuthCodeEntity();
        authCodeEntity.setUserId(userId);
        authCodeEntity.setClientId(clientId);
        authCodeEntity.setCode(code);
        authCodeEntity.setExpireDate(new java.util.Date(System.currentTimeMillis() + 60 * 1000));
        insert(authCodeEntity);
        return code;
    }

    @Override
    public AuthCodeEntity validateCode(String code, String clientId) {
        QueryWrapper<AuthCodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        queryWrapper.eq("client_id", clientId);

        AuthCodeEntity authCodeEntity = baseDao.selectOne(queryWrapper);

        if(authCodeEntity != null) {
            deleteById(authCodeEntity.getId());
        }

        return authCodeEntity;
    }

    private String generateSecureRandomCode() {
        // 使用SecureRandom生成加密安全随机数
        SecureRandom random = new SecureRandom();
        byte[] codeBytes = new byte[16]; // 128位随机数
        random.nextBytes(codeBytes);

        // Base64URL编码（兼容OAuth2规范）
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(codeBytes)
                .substring(0, 22); // 取22字符长度
    }
}
