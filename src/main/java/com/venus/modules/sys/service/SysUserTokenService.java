

package com.venus.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venus.common.utils.R;
import com.venus.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 *
 * @author Tomxuetao
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
