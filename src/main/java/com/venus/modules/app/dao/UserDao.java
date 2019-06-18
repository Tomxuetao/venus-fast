

package com.venus.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venus.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @author Tomxuetao
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
