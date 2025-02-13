package com.venus.modules.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.modules.login.oauth2.TokenGenerator;
import com.venus.modules.oauth.dao.AuthTokenDao;
import com.venus.modules.oauth.entity.AuthTokenEntity;
import com.venus.modules.oauth.service.AuthTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthTokenServiceImpl extends BaseServiceImpl<AuthTokenDao, AuthTokenEntity> implements AuthTokenService {

    @Override
    public void logout(String token) {
        QueryWrapper<AuthTokenEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        AuthTokenEntity authTokenEntity = baseDao.selectOne(queryWrapper);
        if(authTokenEntity != null) {
            deleteById(authTokenEntity.getId());
        }
    }

    @Override
    public AuthTokenEntity validateToken(String token) {
        QueryWrapper<AuthTokenEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        AuthTokenEntity authTokenEntity = baseDao.selectOne(queryWrapper);
        if(authTokenEntity == null) {
            return null;
        } else if(authTokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) {
            // 过期
            return null;
        }
        return authTokenEntity;
    }

    @Override
    public AuthTokenEntity generateToken(Long userId, String clientId) {
        String token;
        String refreshToken;
        QueryWrapper<AuthTokenEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("client_id", clientId);

        AuthTokenEntity authTokenEntity = baseDao.selectOne(queryWrapper);
        // 当前时间
        Date now = new Date();
        // 过期时间 1小时
        Date expireTime = new Date(now.getTime() + 3600 * 1000);
        if(authTokenEntity == null) {
            token = TokenGenerator.generateValue();
            refreshToken = TokenGenerator.generateValue();
            authTokenEntity = new AuthTokenEntity();
            authTokenEntity.setToken(token);
            authTokenEntity.setUserId(userId);
            authTokenEntity.setClientId(clientId);
            authTokenEntity.setExpireDate(expireTime);
            authTokenEntity.setRefreshToken(refreshToken);

            this.insert(authTokenEntity);
        } else {
            // 判断是否过期
            if(authTokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) {
                // 过期
                token = TokenGenerator.generateValue();
                refreshToken = TokenGenerator.generateValue();
            } else {
                // 没过期
                token = authTokenEntity.getToken();
                refreshToken = authTokenEntity.getRefreshToken();
            }
            authTokenEntity.setToken(token);
            authTokenEntity.setUpdateDate(now);
            authTokenEntity.setCreateDate(now);
            authTokenEntity.setExpireDate(expireTime);
            authTokenEntity.setRefreshToken(refreshToken);
            this.updateById(authTokenEntity);
        }

        return authTokenEntity;
    }
}
