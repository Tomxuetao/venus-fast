package com.venus.modules.login.service;

import com.venus.common.base.service.BaseService;
import com.venus.common.utils.Result;
import com.venus.modules.login.entity.SysUserTokenEntity;

public interface SysUserTokenService extends BaseService<SysUserTokenEntity> {

    /**
     * 生成token
     * @param userId  用户ID
     */
    Result createToken(Long userId);

    /**
     * 退出，修改token值
     * @param userId  用户ID
     */
    void logout(Long userId);

}
