package com.venus.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venus.modules.sys.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 *
 * @author Tomxuetao
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);

}
