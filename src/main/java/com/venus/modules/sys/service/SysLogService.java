package com.venus.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.venus.common.utils.PageUtils;
import com.venus.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 * @author Tomxuetao
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
